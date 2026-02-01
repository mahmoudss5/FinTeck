package BankSystem.demo.BusinessLogic.Services;

import BankSystem.demo.DataAccessLayer.DTOs.Transaction.TransactionResponseDTO;
import BankSystem.demo.DataAccessLayer.Entites.Wallet;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    List<TransactionResponseDTO> getWalletTransactions(Long walletId);
    List<TransactionResponseDTO> getAllTransactions();
    String generateMonthlyReport(Long walletId, int month);
    List<TransactionResponseDTO> findTransactionsByStatus(String status);
    void recordTransaction(Wallet sender, Wallet receiver, BigDecimal amount);
}
