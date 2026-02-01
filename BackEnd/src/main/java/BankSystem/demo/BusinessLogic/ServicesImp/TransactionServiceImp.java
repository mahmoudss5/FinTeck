package BankSystem.demo.BusinessLogic.ServicesImp;

import BankSystem.demo.Aspect.Auditing.AuditLog;
import BankSystem.demo.Aspect.Preformance.PerformanceAspect;
import BankSystem.demo.Aspect.Security.OnlyForSameUser;
import BankSystem.demo.Aspect.Security.RequiresAdmin;
import BankSystem.demo.BusinessLogic.Services.TransactionService;
import BankSystem.demo.DataAccessLayer.DTOs.Transaction.TransactionResponseDTO;
import BankSystem.demo.DataAccessLayer.Entites.Status;
import BankSystem.demo.DataAccessLayer.Entites.Transaction;
import BankSystem.demo.DataAccessLayer.Entites.Wallet;
import BankSystem.demo.DataAccessLayer.Repositories.TransactionRepositorie;
import BankSystem.demo.DataAccessLayer.Repositories.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImp implements TransactionService {

    private final TransactionRepositorie transactionRepository;
    private final WalletRepository walletRepository;

    @Override
    @PerformanceAspect
    @OnlyForSameUser
    public List<TransactionResponseDTO> getWalletTransactions(Long walletId) {
        // Validate wallet exists
        if (!walletRepository.existsById(walletId)) {
            throw new RuntimeException("Wallet not found with id: " + walletId);
        }
        
        // Find all transactions where the wallet is either sender or receiver
        return transactionRepository.findBySenderWalletIdOrReceiverWalletId(walletId, walletId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @PerformanceAspect
    @RequiresAdmin
    public List<TransactionResponseDTO> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @PerformanceAspect
    @OnlyForSameUser
    public String generateMonthlyReport(Long walletId, int month) {
        // Validate month
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }

        // Validate wallet exists
        if (!walletRepository.existsById(walletId)) {
            throw new RuntimeException("Wallet not found with id: " + walletId);
        }

        // Get all transactions for the wallet
        List<Transaction> allTransactions = transactionRepository
                .findBySenderWalletIdOrReceiverWalletId(walletId, walletId);

        // Filter transactions for the specified month (current year)
        int currentYear = Year.now().getValue();
        List<Transaction> monthlyTransactions = allTransactions.stream()
                .filter(t -> t.getCreatedAt() != null)
                .filter(t -> t.getCreatedAt().getYear() == currentYear)
                .filter(t -> t.getCreatedAt().getMonthValue() == month)
                .toList();

        // Calculate statistics
        BigDecimal totalSent = BigDecimal.ZERO;
        BigDecimal totalReceived = BigDecimal.ZERO;
        int sentCount = 0;
        int receivedCount = 0;
        int completedCount = 0;
        int pendingCount = 0;
        int failedCount = 0;

        for (Transaction transaction : monthlyTransactions) {
            if (transaction.getSenderWallet() != null && 
                transaction.getSenderWallet().getId().equals(walletId)) {
                totalSent = totalSent.add(transaction.getAmount());
                sentCount++;
            }
            if (transaction.getReceiverWallet() != null && 
                transaction.getReceiverWallet().getId().equals(walletId)) {
                totalReceived = totalReceived.add(transaction.getAmount());
                receivedCount++;
            }

            switch (transaction.getStatus()) {
                case COMPLETED -> completedCount++;
                case PENDING -> pendingCount++;
                case FAILED, CANCELLED -> failedCount++;
            }
        }

        BigDecimal netBalance = totalReceived.subtract(totalSent);
        String monthName = Month.of(month).name();

        // Generate report
        StringBuilder report = new StringBuilder();
        report.append("=".repeat(50)).append("\n");
        report.append("       MONTHLY TRANSACTION REPORT\n");
        report.append("=".repeat(50)).append("\n");
        report.append(String.format("Wallet ID: %d\n", walletId));
        report.append(String.format("Period: %s %d\n", monthName, currentYear));
        report.append(String.format("Generated: %s\n", LocalDateTime.now().toString()));
        report.append("-".repeat(50)).append("\n\n");
        
        report.append("SUMMARY:\n");
        report.append(String.format("  Total Transactions: %d\n", monthlyTransactions.size()));
        report.append(String.format("  - Sent: %d (Total: $%.2f)\n", sentCount, totalSent));
        report.append(String.format("  - Received: %d (Total: $%.2f)\n", receivedCount, totalReceived));
        report.append(String.format("  Net Balance Change: $%.2f\n", netBalance));
        report.append("\n");
        
        report.append("STATUS BREAKDOWN:\n");
        report.append(String.format("  - Completed: %d\n", completedCount));
        report.append(String.format("  - Pending: %d\n", pendingCount));
        report.append(String.format("  - Failed/Cancelled: %d\n", failedCount));
        report.append("\n");
        
        if (!monthlyTransactions.isEmpty()) {
            report.append("TRANSACTION DETAILS:\n");
            report.append("-".repeat(50)).append("\n");
            for (Transaction t : monthlyTransactions) {
                String type = (t.getSenderWallet() != null && 
                              t.getSenderWallet().getId().equals(walletId)) ? "SENT" : "RECEIVED";
                report.append(String.format("  [%s] ID: %d | Amount: $%.2f | Status: %s | Date: %s\n",
                        type,
                        t.getId(),
                        t.getAmount(),
                        t.getStatus(),
                        t.getCreatedAt().toLocalDate()));
            }
        } else {
            report.append("No transactions found for this period.\n");
        }
        
        report.append("=".repeat(50)).append("\n");

        return report.toString();
    }

    @Override
    @RequiresAdmin
    public List<TransactionResponseDTO> findTransactionsByStatus(String status) {
        // Parse the status string to enum
        Status transactionStatus;
        try {
            transactionStatus = Status.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + status + 
                    ". Valid values are: PENDING, COMPLETED, FAILED, CANCELLED");
        }

        return transactionRepository.findByStatus(transactionStatus)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @AuditLog
    public void recordTransaction(Wallet sender, Wallet receiver, BigDecimal amount) {
        Transaction transaction = Transaction.builder()
                .senderWallet(sender)
                .receiverWallet(receiver)
                .amount(amount)
                .status(Status.COMPLETED) // Assuming transaction is completed when recorded
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);
    }

    // Helper method to convert Transaction to DTO
    private TransactionResponseDTO convertToDTO(Transaction transaction) {
        return TransactionResponseDTO.builder()
                .senderWalletId(transaction.getSenderWallet() != null ? 
                        transaction.getSenderWallet().getId() : null)
                .senderUserName(transaction.getSenderWallet() != null && 
                        transaction.getSenderWallet().getUser() != null ? 
                        transaction.getSenderWallet().getUser().getUserName() : null)
                .receiverUserName(transaction.getReceiverWallet() != null && 
                        transaction.getReceiverWallet().getUser() != null ? 
                        transaction.getReceiverWallet().getUser().getUserName() : null)
                .amount(transaction.getAmount())
                .status(transaction.getStatus())
                .currency(transaction.getSenderWallet() != null ?
                        transaction.getSenderWallet().getWalletCurrency() : null)
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
