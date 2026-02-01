package BankSystem.demo.BusinessLogic.ServicesImp;

import BankSystem.demo.DataAccessLayer.DTOs.Transaction.TransactionResponseDTO;
import BankSystem.demo.DataAccessLayer.Entites.Status;
import BankSystem.demo.DataAccessLayer.Entites.Transaction;
import BankSystem.demo.DataAccessLayer.Entites.User;
import BankSystem.demo.DataAccessLayer.Entites.Wallet;
import BankSystem.demo.DataAccessLayer.Entites.WalletCurrency;
import BankSystem.demo.DataAccessLayer.Repositories.TransactionRepositorie;
import BankSystem.demo.DataAccessLayer.Repositories.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImpTest {

    @Mock
    private TransactionRepositorie transactionRepository;

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private TransactionServiceImp transactionService;

    private User testUser;
    private Wallet senderWallet;
    private Wallet receiverWallet;
    private Transaction testTransaction;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .userName("testuser")
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .build();

        senderWallet = Wallet.builder()
                .id(1L)
                .user(testUser)
                .walletCurrency(WalletCurrency.USD)
                .balance(BigDecimal.valueOf(1000.00))
                .active(true)
                .build();

        receiverWallet = Wallet.builder()
                .id(2L)
                .user(testUser)
                .walletCurrency(WalletCurrency.USD)
                .balance(BigDecimal.valueOf(500.00))
                .active(true)
                .build();

        testTransaction = Transaction.builder()
                .id(1L)
                .senderWallet(senderWallet)
                .receiverWallet(receiverWallet)
                .amount(BigDecimal.valueOf(100.00))
                .status(Status.COMPLETED)
                .createdAt(LocalDateTime.now())
                .build();
    }

    // ==================== GET WALLET TRANSACTIONS TESTS ====================

    @Test
    void testGetWalletTransactions_Success() {
        // Arrange
        when(walletRepository.existsById(1L)).thenReturn(true);
        when(transactionRepository.findBySenderWalletIdOrReceiverWalletId(1L, 1L))
                .thenReturn(Arrays.asList(testTransaction));

        // Act
        List<TransactionResponseDTO> result = transactionService.getWalletTransactions(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(walletRepository, times(1)).existsById(1L);
        verify(transactionRepository, times(1)).findBySenderWalletIdOrReceiverWalletId(1L, 1L);
    }

    @Test
    void testGetWalletTransactions_WalletNotFound() {
        // Arrange
        when(walletRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> transactionService.getWalletTransactions(999L));

        assertEquals("Wallet not found with id: 999", exception.getMessage());
        verify(walletRepository, times(1)).existsById(999L);
        verify(transactionRepository, never()).findBySenderWalletIdOrReceiverWalletId(anyLong(), anyLong());
    }

    @Test
    void testGetWalletTransactions_EmptyList() {
        // Arrange
        when(walletRepository.existsById(1L)).thenReturn(true);
        when(transactionRepository.findBySenderWalletIdOrReceiverWalletId(1L, 1L))
                .thenReturn(Collections.emptyList());

        // Act
        List<TransactionResponseDTO> result = transactionService.getWalletTransactions(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ==================== GET ALL TRANSACTIONS TESTS ====================

    @Test
    void testGetAllTransactions_Success() {
        // Arrange
        Transaction transaction2 = Transaction.builder()
                .id(2L)
                .senderWallet(receiverWallet)
                .receiverWallet(senderWallet)
                .amount(BigDecimal.valueOf(50.00))
                .status(Status.COMPLETED)
                .createdAt(LocalDateTime.now())
                .build();

        when(transactionRepository.findAll()).thenReturn(Arrays.asList(testTransaction, transaction2));

        // Act
        List<TransactionResponseDTO> result = transactionService.getAllTransactions();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void testGetAllTransactions_EmptyList() {
        // Arrange
        when(transactionRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<TransactionResponseDTO> result = transactionService.getAllTransactions();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ==================== FIND TRANSACTIONS BY STATUS TESTS ====================

    @Test
    void testFindTransactionsByStatus_Success() {
        // Arrange
        when(transactionRepository.findByStatus(Status.COMPLETED))
                .thenReturn(Arrays.asList(testTransaction));

        // Act
        List<TransactionResponseDTO> result = transactionService.findTransactionsByStatus("COMPLETED");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Status.COMPLETED, result.get(0).getStatus());
        verify(transactionRepository, times(1)).findByStatus(Status.COMPLETED);
    }

    @Test
    void testFindTransactionsByStatus_CaseInsensitive() {
        // Arrange
        when(transactionRepository.findByStatus(Status.PENDING))
                .thenReturn(Collections.emptyList());

        // Act
        List<TransactionResponseDTO> result = transactionService.findTransactionsByStatus("pending");

        // Assert
        assertNotNull(result);
        verify(transactionRepository, times(1)).findByStatus(Status.PENDING);
    }

    @Test
    void testFindTransactionsByStatus_InvalidStatus() {
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> transactionService.findTransactionsByStatus("INVALID_STATUS"));

        assertTrue(exception.getMessage().contains("Invalid status: INVALID_STATUS"));
        verify(transactionRepository, never()).findByStatus(any());
    }

    // ==================== RECORD TRANSACTION TESTS ====================

    @Test
    void testRecordTransaction_Success() {
        // Arrange
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);

        // Act
        transactionService.recordTransaction(senderWallet, receiverWallet, BigDecimal.valueOf(100.00));

        // Assert
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testRecordTransaction_CorrectData() {
        // Arrange
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction saved = invocation.getArgument(0);
            assertEquals(senderWallet, saved.getSenderWallet());
            assertEquals(receiverWallet, saved.getReceiverWallet());
            assertEquals(BigDecimal.valueOf(200.00), saved.getAmount());
            assertEquals(Status.COMPLETED, saved.getStatus());
            return saved;
        });

        // Act
        transactionService.recordTransaction(senderWallet, receiverWallet, BigDecimal.valueOf(200.00));

        // Assert
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    // ==================== GENERATE MONTHLY REPORT TESTS ====================

    @Test
    void testGenerateMonthlyReport_Success() {
        // Arrange
        when(walletRepository.existsById(1L)).thenReturn(true);
        when(transactionRepository.findBySenderWalletIdOrReceiverWalletId(1L, 1L))
                .thenReturn(Arrays.asList(testTransaction));

        // Act
        String report = transactionService.generateMonthlyReport(1L, LocalDateTime.now().getMonthValue());

        // Assert
        assertNotNull(report);
        assertTrue(report.contains("MONTHLY TRANSACTION REPORT"));
        assertTrue(report.contains("Wallet ID: 1"));
        verify(walletRepository, times(1)).existsById(1L);
    }

    @Test
    void testGenerateMonthlyReport_WalletNotFound() {
        // Arrange
        when(walletRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> transactionService.generateMonthlyReport(999L, 1));

        assertEquals("Wallet not found with id: 999", exception.getMessage());
    }

    @Test
    void testGenerateMonthlyReport_InvalidMonth_TooLow() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transactionService.generateMonthlyReport(1L, 0));

        assertEquals("Month must be between 1 and 12", exception.getMessage());
    }

    @Test
    void testGenerateMonthlyReport_InvalidMonth_TooHigh() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transactionService.generateMonthlyReport(1L, 13));

        assertEquals("Month must be between 1 and 12", exception.getMessage());
    }

    @Test
    void testGenerateMonthlyReport_NoTransactions() {
        // Arrange
        when(walletRepository.existsById(1L)).thenReturn(true);
        when(transactionRepository.findBySenderWalletIdOrReceiverWalletId(1L, 1L))
                .thenReturn(Collections.emptyList());

        // Act
        String report = transactionService.generateMonthlyReport(1L, 1);

        // Assert
        assertNotNull(report);
        assertTrue(report.contains("No transactions found for this period."));
    }
}
