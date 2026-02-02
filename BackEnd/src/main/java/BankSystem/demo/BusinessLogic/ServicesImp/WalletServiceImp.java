package BankSystem.demo.BusinessLogic.ServicesImp;

import BankSystem.demo.Aspect.Auditing.AuditLog;
import BankSystem.demo.Aspect.Preformance.PerformanceAspect;
import BankSystem.demo.Aspect.Security.OnlyForSameUser;
import BankSystem.demo.Aspect.Security.RequiresAdmin;
import BankSystem.demo.BusinessLogic.Services.TransactionService;
import BankSystem.demo.BusinessLogic.Services.WalletService;
import BankSystem.demo.Config.CurrentUserProvider;
import BankSystem.demo.DataAccessLayer.DTOs.Transaction.TransactionResponseDTO;
import BankSystem.demo.DataAccessLayer.DTOs.Wallet.WalletRequestDTO;
import BankSystem.demo.DataAccessLayer.DTOs.Wallet.WalletResponseDTO;
import BankSystem.demo.DataAccessLayer.Entites.*;
import BankSystem.demo.DataAccessLayer.Repositories.UserRepositorie;
import BankSystem.demo.DataAccessLayer.Repositories.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import BankSystem.demo.DataAccessLayer.DTOs.Transaction.TransactionRequestDTO;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j // for logging
public class WalletServiceImp implements WalletService {

    private final WalletRepository walletRepository;
    private final UserRepositorie userRepositorie;
    private final TransactionService transactionService;
    private final CurrentUserProvider currentUserProvider;

    public WalletResponseDTO convertWalletToResponseDTO(Wallet wallet) {
        return WalletResponseDTO.builder()
                .id(wallet.getId())
                .userId(wallet.getUser().getId())
                .userName(wallet.getUser().getUserName())
                .currency(wallet.getWalletCurrency())
                .balance(wallet.getBalance())
                .active(wallet.getActive())
                .createdAt(wallet.getCreatedAt())
                .build();
    }

    @Override
    @AuditLog
    public WalletResponseDTO createWallet(WalletRequestDTO walletRequestDTO) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("User not authenticated");
        }
        User user = userRepositorie.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Boolean WalletExists = walletRepository.existsByUserIdAndWalletCurrency(user.getId(),
                walletRequestDTO.getCurrency());
        if (WalletExists) {
            throw new RuntimeException("Wallet with this currency already exists for the user");
        }

        Wallet wallet = Wallet.builder()
                .user(user)
                .walletCurrency(walletRequestDTO.getCurrency())
                .balance(walletRequestDTO.getInitialBalance())
                .build();

        walletRepository.save(wallet);

        return convertWalletToResponseDTO(wallet);
    }


    private TransactionResponseDTO convertTransactionToTransactionResponseDTO(TransactionRequestDTO transaction,String SenderUserName) {
        return TransactionResponseDTO.builder()
                .senderWalletId(transaction.getSenderWalletId())
                .receiverUserName(transaction.getReceiverUserName())
                .senderUserName(SenderUserName)
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .status(Status.COMPLETED)
                .createdAt(java.time.LocalDateTime.now())
                .build();
    }


    @Override
    @Transactional // to manage transactions and support rollback on failure
    @Retryable(retryFor = ObjectOptimisticLockingFailureException.class, // the errorType wich were retrying for
            maxAttempts = 3, backoff = @Backoff(delay = 1000) // wait time between retries in milliseconds
    )
    @PerformanceAspect
    @AuditLog
    public TransactionResponseDTO TransferFunds(TransactionRequestDTO transactionRequestDTO) {

        Long currentUserId = currentUserProvider.getCurrentUserId();

        if (currentUserId == null) {
            throw new RuntimeException("User not authenticated");
        }

        Long fromWalletId = transactionRequestDTO.getSenderWalletId();
        Wallet Sender = walletRepository.findById(fromWalletId)
                .orElseThrow(() -> new RuntimeException("Source wallet not found"));

        Double amount = transactionRequestDTO.getAmount().doubleValue();
        if (!Sender.getUser().getId().equals(currentUserId)) {
            throw new RuntimeException("Source wallet does not belong to the current user");
        }

        Wallet Reciver=walletRepository.findByWalletCurrencyAndUserUserName(WalletCurrency.valueOf(transactionRequestDTO.getCurrency().name()),transactionRequestDTO.getReceiverUserName());
        if (Reciver==null){
            throw new RuntimeException("Receiver wallet not found");
        }


        if (!Sender.getActive() || !Reciver.getActive()) {
            throw new RuntimeException("One or both wallets are inactive");
        }
        if (Sender.getBalance().doubleValue() < amount) {
            throw new RuntimeException("Insufficient funds in the source wallet");
        }

        Sender.setBalance(Sender.getBalance().subtract(java.math.BigDecimal.valueOf(amount)));
        Reciver.setBalance(Reciver.getBalance().add(java.math.BigDecimal.valueOf(amount)));

        // save the wallets to the database
        // this will trigger the optimistic locking mechanism if there are concurrent
        // updates

        walletRepository.save(Sender);
        walletRepository.save(Reciver);
        // record the transaction
        transactionService.recordTransaction(Sender, Reciver, java.math.BigDecimal.valueOf(amount));

        return convertTransactionToTransactionResponseDTO(transactionRequestDTO,Sender.getUser().getUserName());
    }

    @Override
    @OnlyForSameUser
    public WalletResponseDTO getWalletById(Long id) {
        return convertWalletToResponseDTO(
                walletRepository.findById(id).orElseThrow(() -> new RuntimeException("Wallet not found")));
    }

    @Override
    @PerformanceAspect
    @RequiresAdmin
    public List<WalletResponseDTO> getAllWallets() {
        List<Wallet> wallets = walletRepository.findAll();
        return wallets.stream().map(this::convertWalletToResponseDTO).toList();
    }

    @Override
    @OnlyForSameUser
   @AuditLog
    public List<WalletResponseDTO> getAllWalletsForCurrentUser() {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("User not authenticated");
        }
        List<Wallet> wallets = walletRepository.findByUserId(currentUserId);
        return wallets.stream().map(this::convertWalletToResponseDTO).toList();
    }

    @Override
    public WalletResponseDTO deactivateWallet(Long id) {
        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        Boolean walletActive = wallet.getActive();
        if (!walletActive) {
            throw new RuntimeException("Wallet is already deactivated");
        }
        wallet.setActive(false);

        walletRepository.save(wallet);
        return convertWalletToResponseDTO(wallet);
    }

    @Override
    @Transactional
    @RequiresAdmin
    @AuditLog
    public WalletResponseDTO deleteWallet(Long id) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new RuntimeException("Wallet not found"));
        walletRepository.delete(wallet);
        return convertWalletToResponseDTO(wallet);
    }

    @Override
    @RequiresAdmin
    @AuditLog
    public List<WalletResponseDTO> getAllWalletsByUserId(Long userId) {
        if (userId == null) {
            throw new RuntimeException("User ID cannot be null");
        }
        List<Wallet> wallets = walletRepository.findByUserId(userId);
        if (wallets.isEmpty()) {
            throw new RuntimeException("No wallets found for user ID: " + userId);
        }
        return wallets.stream()
                .map(this::convertWalletToResponseDTO)
                .toList();
    }

    @Override
    public Boolean existsByUserIdAndWalletCurrency(Long userId, WalletCurrency walletCurrency) {
        return walletRepository.existsByUserIdAndWalletCurrency(userId, walletCurrency);
    }

    // this function will be called if all retry attempts fail
    @Recover
    public Boolean recover(ObjectOptimisticLockingFailureException e, Long fromWalletId, Long toWalletId,
            Double amount) {
        log.error("TransferFunds failed after retries due to concurrent modification: {}", e.getMessage());
        throw new RuntimeException("Transfer failed due to high concurrency. Please try again later.");
    }


}
