package BankSystem.demo.BusinessLogic.ServicesImp;

import BankSystem.demo.Config.CurrentUserProvider;
import BankSystem.demo.DataAccessLayer.DTOs.Transaction.TransactionRequestDTO;
import BankSystem.demo.DataAccessLayer.DTOs.Wallet.WalletRequestDTO;
import BankSystem.demo.DataAccessLayer.DTOs.Wallet.WalletResponseDTO;
import BankSystem.demo.DataAccessLayer.Entites.User;
import BankSystem.demo.DataAccessLayer.Entites.Wallet;
import BankSystem.demo.DataAccessLayer.Entites.WalletCurrency;
import BankSystem.demo.DataAccessLayer.Repositories.UserRepositorie;
import BankSystem.demo.DataAccessLayer.Repositories.WalletRepository;
import BankSystem.demo.BusinessLogic.Services.TransactionService;
import BankSystem.demo.DataAccessLayer.DTOs.Transaction.TransactionResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceImpTest {

        @Mock
        private WalletRepository walletRepository;

        @Mock
        private UserRepositorie userRepositorie;

        @Mock
        private TransactionService transactionService;

        @Mock
        private CurrentUserProvider currentUserProvider;

        @InjectMocks
        private WalletServiceImp walletService;

        private User testUser;
        private Wallet testWallet;
        private Wallet senderWallet;
        private Wallet receiverWallet;

        @BeforeEach
        void setUp() {
                testUser = User.builder()
                                .id(1L)
                                .firstName("John")
                                .lastName("Doe")
                                .userName("johndoe")
                                .email("john@gmail.com")
                                .password("hashedPassword123")
                                .build();

                testWallet = Wallet.builder()
                                .id(1L)
                                .user(testUser)
                                .walletCurrency(WalletCurrency.USD)
                                .balance(BigDecimal.valueOf(1000.00))
                                .active(true)
                                .createdAt(LocalDateTime.now())
                                .version(0L)
                                .build();

                User sender = User.builder()
                                .id(1L)
                                .userName("sender")
                                .email("sender@gmail.com")
                                .password("password")
                                .build();

                User receiver = User.builder()
                                .id(2L)
                                .userName("receiver")
                                .email("receiver@gmail.com")
                                .password("password")
                                .build();

                senderWallet = Wallet.builder()
                                .id(1L)
                                .user(sender)
                                .walletCurrency(WalletCurrency.USD)
                                .balance(BigDecimal.valueOf(1000.00))
                                .active(true)
                                .version(0L)
                                .build();

                receiverWallet = Wallet.builder()
                                .id(2L)
                                .user(receiver)
                                .walletCurrency(WalletCurrency.USD)
                                .balance(BigDecimal.valueOf(500.00))
                                .active(true)
                                .version(0L)
                                .build();
        }

        // ==================== createWallet Tests ====================

        @Test
        void createWallet_Success() {
                WalletRequestDTO requestDTO = WalletRequestDTO.builder()
                                .currency(WalletCurrency.USD)
                                .initialBalance(BigDecimal.valueOf(500.00))
                                .build();

                when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
                when(userRepositorie.findById(1L)).thenReturn(Optional.of(testUser));
                when(walletRepository.existsByUserIdAndWalletCurrency(1L, WalletCurrency.USD)).thenReturn(false);
                when(walletRepository.save(any(Wallet.class))).thenReturn(testWallet);

                WalletResponseDTO response = walletService.createWallet(requestDTO);

                assertNotNull(response);
                assertEquals(testUser.getId(), response.getUserId());
                assertEquals(testUser.getUserName(), response.getUserName());
                assertEquals(WalletCurrency.USD, response.getCurrency());
                verify(walletRepository, times(1)).save(any(Wallet.class));
        }

        @Test
        void createWallet_UserNotFound_ThrowsException() {
                WalletRequestDTO requestDTO = WalletRequestDTO.builder()
                                .currency(WalletCurrency.USD)
                                .initialBalance(BigDecimal.valueOf(500.00))
                                .build();

                when(currentUserProvider.getCurrentUserId()).thenReturn(999L);
                when(userRepositorie.findById(999L)).thenReturn(Optional.empty());

                RuntimeException exception = assertThrows(RuntimeException.class,
                                () -> walletService.createWallet(requestDTO));

                assertEquals("User not found", exception.getMessage());
                verify(walletRepository, never()).save(any(Wallet.class));
        }

        @Test
        void createWallet_WalletAlreadyExists_ThrowsException() {
                WalletRequestDTO requestDTO = WalletRequestDTO.builder()
                                .currency(WalletCurrency.USD)
                                .initialBalance(BigDecimal.valueOf(500.00))
                                .build();

                when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
                when(userRepositorie.findById(1L)).thenReturn(Optional.of(testUser));
                when(walletRepository.existsByUserIdAndWalletCurrency(1L, WalletCurrency.USD)).thenReturn(true);

                RuntimeException exception = assertThrows(RuntimeException.class,
                                () -> walletService.createWallet(requestDTO));

                assertEquals("Wallet with this currency already exists for the user", exception.getMessage());
                verify(walletRepository, never()).save(any(Wallet.class));
        }

        @Test
        void createWallet_DifferentCurrency_Success() {
                WalletRequestDTO requestDTO = WalletRequestDTO.builder()
                                .currency(WalletCurrency.EGP)
                                .initialBalance(BigDecimal.valueOf(5000.00))
                                .build();

                Wallet egpWallet = Wallet.builder()
                                .id(2L)
                                .user(testUser)
                                .walletCurrency(WalletCurrency.EGP)
                                .balance(BigDecimal.valueOf(5000.00))
                                .active(true)
                                .createdAt(LocalDateTime.now())
                                .version(0L)
                                .build();

                when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
                when(userRepositorie.findById(1L)).thenReturn(Optional.of(testUser));
                when(walletRepository.existsByUserIdAndWalletCurrency(1L, WalletCurrency.EGP)).thenReturn(false);
                when(walletRepository.save(any(Wallet.class))).thenReturn(egpWallet);

                WalletResponseDTO response = walletService.createWallet(requestDTO);

                assertNotNull(response);
                assertEquals(WalletCurrency.EGP, response.getCurrency());
                verify(walletRepository, times(1)).save(any(Wallet.class));
        }

        // ==================== TransferFunds Tests ====================

        @Test
        void transferFunds_Success() {
                when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
                when(walletRepository.findById(1L)).thenReturn(Optional.of(senderWallet));
                when(walletRepository.findByWalletCurrencyAndUserUserName(WalletCurrency.USD, "receiver"))
                                .thenReturn(receiverWallet);

                TransactionRequestDTO requestDTO = TransactionRequestDTO.builder()
                                .senderWalletId(1L)
                                .receiverUserName("receiver")
                                .currency(WalletCurrency.USD)
                                .amount(BigDecimal.valueOf(200.00))
                                .build();

                TransactionResponseDTO result = walletService.TransferFunds(requestDTO);

                assertNotNull(result);
                assertEquals(1L, result.getSenderWalletId());
                assertEquals("receiver", result.getReceiverUserName());
                assertEquals(BigDecimal.valueOf(200.00), result.getAmount());
                assertEquals(WalletCurrency.USD, result.getCurrency());
                assertEquals(BigDecimal.valueOf(800.00), senderWallet.getBalance());
                assertEquals(BigDecimal.valueOf(700.00), receiverWallet.getBalance());
                verify(walletRepository, times(2)).save(any(Wallet.class));
        }

        @Test
        void transferFunds_SourceWalletNotFound_ThrowsException() {
                when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
                when(walletRepository.findById(999L)).thenReturn(Optional.empty());

                TransactionRequestDTO requestDTO = TransactionRequestDTO.builder()
                                .senderWalletId(999L)
                                .receiverUserName("receiver")
                                .currency(WalletCurrency.USD)
                                .amount(BigDecimal.valueOf(100.00))
                                .build();

                RuntimeException exception = assertThrows(RuntimeException.class,
                                () -> walletService.TransferFunds(requestDTO));

                assertEquals("Source wallet not found", exception.getMessage());
                verify(walletRepository, never()).save(any(Wallet.class));
        }

        @Test
        void transferFunds_ReceiverWalletNotFound_ThrowsException() {
                when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
                when(walletRepository.findById(1L)).thenReturn(Optional.of(senderWallet));
                when(walletRepository.findByWalletCurrencyAndUserUserName(WalletCurrency.USD, "unknownuser"))
                                .thenReturn(null);

                TransactionRequestDTO requestDTO = TransactionRequestDTO.builder()
                                .senderWalletId(1L)
                                .receiverUserName("unknownuser")
                                .currency(WalletCurrency.USD)
                                .amount(BigDecimal.valueOf(100.00))
                                .build();

                RuntimeException exception = assertThrows(RuntimeException.class,
                                () -> walletService.TransferFunds(requestDTO));

                assertEquals("Receiver wallet not found", exception.getMessage());
                verify(walletRepository, never()).save(any(Wallet.class));
        }

        @Test
        void transferFunds_SenderWalletInactive_ThrowsException() {
                senderWallet.setActive(false);
                when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
                when(walletRepository.findById(1L)).thenReturn(Optional.of(senderWallet));
                when(walletRepository.findByWalletCurrencyAndUserUserName(WalletCurrency.USD, "receiver"))
                                .thenReturn(receiverWallet);

                TransactionRequestDTO requestDTO = TransactionRequestDTO.builder()
                                .senderWalletId(1L)
                                .receiverUserName("receiver")
                                .currency(WalletCurrency.USD)
                                .amount(BigDecimal.valueOf(100.00))
                                .build();

                RuntimeException exception = assertThrows(RuntimeException.class,
                                () -> walletService.TransferFunds(requestDTO));

                assertEquals("One or both wallets are inactive", exception.getMessage());
                verify(walletRepository, never()).save(any(Wallet.class));
        }

        @Test
        void transferFunds_ReceiverWalletInactive_ThrowsException() {
                receiverWallet.setActive(false);
                when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
                when(walletRepository.findById(1L)).thenReturn(Optional.of(senderWallet));
                when(walletRepository.findByWalletCurrencyAndUserUserName(WalletCurrency.USD, "receiver"))
                                .thenReturn(receiverWallet);

                TransactionRequestDTO requestDTO = TransactionRequestDTO.builder()
                                .senderWalletId(1L)
                                .receiverUserName("receiver")
                                .currency(WalletCurrency.USD)
                                .amount(BigDecimal.valueOf(100.00))
                                .build();

                RuntimeException exception = assertThrows(RuntimeException.class,
                                () -> walletService.TransferFunds(requestDTO));

                assertEquals("One or both wallets are inactive", exception.getMessage());
                verify(walletRepository, never()).save(any(Wallet.class));
        }

        @Test
        void transferFunds_BothWalletsInactive_ThrowsException() {
                senderWallet.setActive(false);
                receiverWallet.setActive(false);
                when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
                when(walletRepository.findById(1L)).thenReturn(Optional.of(senderWallet));
                when(walletRepository.findByWalletCurrencyAndUserUserName(WalletCurrency.USD, "receiver"))
                                .thenReturn(receiverWallet);

                TransactionRequestDTO requestDTO = TransactionRequestDTO.builder()
                                .senderWalletId(1L)
                                .receiverUserName("receiver")
                                .currency(WalletCurrency.USD)
                                .amount(BigDecimal.valueOf(100.00))
                                .build();

                RuntimeException exception = assertThrows(RuntimeException.class,
                                () -> walletService.TransferFunds(requestDTO));

                assertEquals("One or both wallets are inactive", exception.getMessage());
                verify(walletRepository, never()).save(any(Wallet.class));
        }

        @Test
        void transferFunds_InsufficientFunds_ThrowsException() {
                when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
                when(walletRepository.findById(1L)).thenReturn(Optional.of(senderWallet));
                when(walletRepository.findByWalletCurrencyAndUserUserName(WalletCurrency.USD, "receiver"))
                                .thenReturn(receiverWallet);

                TransactionRequestDTO requestDTO = TransactionRequestDTO.builder()
                                .senderWalletId(1L)
                                .receiverUserName("receiver")
                                .currency(WalletCurrency.USD)
                                .amount(BigDecimal.valueOf(2000.00))
                                .build();

                RuntimeException exception = assertThrows(RuntimeException.class,
                                () -> walletService.TransferFunds(requestDTO));

                assertEquals("Insufficient funds in the source wallet", exception.getMessage());
                verify(walletRepository, never()).save(any(Wallet.class));
        }

        @Test
        void transferFunds_ExactBalance_Success() {
                when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
                when(walletRepository.findById(1L)).thenReturn(Optional.of(senderWallet));
                when(walletRepository.findByWalletCurrencyAndUserUserName(WalletCurrency.USD, "receiver"))
                                .thenReturn(receiverWallet);

                TransactionRequestDTO requestDTO = TransactionRequestDTO.builder()
                                .senderWalletId(1L)
                                .receiverUserName("receiver")
                                .currency(WalletCurrency.USD)
                                .amount(BigDecimal.valueOf(1000.00))
                                .build();

                TransactionResponseDTO result = walletService.TransferFunds(requestDTO);

                assertNotNull(result);
                assertEquals(BigDecimal.valueOf(0.0), senderWallet.getBalance());
                assertEquals(BigDecimal.valueOf(1500.00), receiverWallet.getBalance());
                verify(walletRepository, times(2)).save(any(Wallet.class));
        }

        @Test
        void transferFunds_SmallAmount_Success() {
                when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
                when(walletRepository.findById(1L)).thenReturn(Optional.of(senderWallet));
                when(walletRepository.findByWalletCurrencyAndUserUserName(WalletCurrency.USD, "receiver"))
                                .thenReturn(receiverWallet);

                TransactionRequestDTO requestDTO = TransactionRequestDTO.builder()
                                .senderWalletId(1L)
                                .receiverUserName("receiver")
                                .currency(WalletCurrency.USD)
                                .amount(BigDecimal.valueOf(0.01))
                                .build();

                TransactionResponseDTO result = walletService.TransferFunds(requestDTO);

                assertNotNull(result);
                verify(walletRepository, times(2)).save(any(Wallet.class));
        }

        @Test
        void transferFunds_SenderWalletNotOwnedByCurrentUser_ThrowsException() {
                when(currentUserProvider.getCurrentUserId()).thenReturn(999L);
                when(walletRepository.findById(1L)).thenReturn(Optional.of(senderWallet));

                TransactionRequestDTO requestDTO = TransactionRequestDTO.builder()
                                .senderWalletId(1L)
                                .receiverUserName("receiver")
                                .currency(WalletCurrency.USD)
                                .amount(BigDecimal.valueOf(100.00))
                                .build();

                RuntimeException exception = assertThrows(RuntimeException.class,
                                () -> walletService.TransferFunds(requestDTO));

                assertEquals("Source wallet does not belong to the current user", exception.getMessage());
                verify(walletRepository, never()).save(any(Wallet.class));
        }

        // ==================== deactivateWallet Tests ====================

        @Test
        void deactivateWallet_Success() {
                when(walletRepository.findById(1L)).thenReturn(Optional.of(testWallet));

                WalletResponseDTO response = walletService.deactivateWallet(1L);

                assertNotNull(response);
                assertFalse(response.getActive());
                verify(walletRepository, times(1)).save(any(Wallet.class));
        }

        @Test
        void deactivateWallet_NotFound_ThrowsException() {
                when(walletRepository.findById(999L)).thenReturn(Optional.empty());

                RuntimeException exception = assertThrows(RuntimeException.class,
                                () -> walletService.deactivateWallet(999L));

                assertEquals("Wallet not found", exception.getMessage());
                verify(walletRepository, never()).save(any(Wallet.class));
        }

        @Test
        void deactivateWallet_AlreadyDeactivated_ThrowsException() {
                testWallet.setActive(false);
                when(walletRepository.findById(1L)).thenReturn(Optional.of(testWallet));

                RuntimeException exception = assertThrows(RuntimeException.class,
                                () -> walletService.deactivateWallet(1L));

                assertEquals("Wallet is already deactivated", exception.getMessage());
                verify(walletRepository, never()).save(any(Wallet.class));
        }

        // ==================== Recover Tests ====================

        @Test
        void recover_ThrowsRuntimeException() {
                ObjectOptimisticLockingFailureException lockException = new ObjectOptimisticLockingFailureException(
                                Wallet.class, 1L);

                RuntimeException exception = assertThrows(RuntimeException.class,
                                () -> walletService.recover(lockException, 1L, 2L, 100.00));

                assertEquals("Transfer failed due to high concurrency. Please try again later.",
                                exception.getMessage());
        }
}
