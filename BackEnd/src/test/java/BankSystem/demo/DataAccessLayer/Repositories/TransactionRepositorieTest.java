package BankSystem.demo.DataAccessLayer.Repositories;

import BankSystem.demo.DataAccessLayer.Entites.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
class TransactionRepositorieTest {

    @Autowired
    private TransactionRepositorie transactionRepositorie;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepositorie userRepositorie;

    private User testUser;
    private User anotherUser;
    private Wallet senderWallet;
    private Wallet receiverWallet;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .userName("sender_user")
                .email("sender@example.com")
                .firstName("Sender")
                .lastName("User")
                .password("hashed_password")
                .build();
        userRepositorie.save(testUser);

        anotherUser = User.builder()
                .userName("receiver_user")
                .email("receiver@example.com")
                .firstName("Receiver")
                .lastName("User")
                .password("hashed_password")
                .build();
        userRepositorie.save(anotherUser);

        senderWallet = Wallet.builder()
                .user(testUser)
                .walletCurrency(WalletCurrency.USD)
                .balance(new BigDecimal("5000.0000"))
                .active(true)
                .build();
        walletRepository.save(senderWallet);

        receiverWallet = Wallet.builder()
                .user(anotherUser)
                .walletCurrency(WalletCurrency.USD)
                .balance(new BigDecimal("1000.0000"))
                .active(true)
                .build();
        walletRepository.save(receiverWallet);
    }

    @AfterEach
    void tearDown() {
        transactionRepositorie.deleteAll();
        walletRepository.deleteAll();
        userRepositorie.deleteAll();
    }

    @Test
    void itShouldSaveTransaction() {
        // given
        Transaction transaction = Transaction.builder()
                .senderWallet(senderWallet)
                .receiverWallet(receiverWallet)
                .amount(new BigDecimal("100.0000"))
                .status(Status.PENDING)
                .build();

        // when
        Transaction savedTransaction = transactionRepositorie.save(transaction);

        // then
        assertThat(savedTransaction).isNotNull();
        assertThat(savedTransaction.getId()).isNotNull();
        assertThat(savedTransaction.getAmount()).isEqualByComparingTo(new BigDecimal("100.0000"));
        assertThat(savedTransaction.getStatus()).isEqualTo(Status.PENDING);
        assertThat(savedTransaction.getSenderWallet().getId()).isEqualTo(senderWallet.getId());
        assertThat(savedTransaction.getReceiverWallet().getId()).isEqualTo(receiverWallet.getId());
    }

    @Test
    void itShouldFindTransactionById() {
        // given
        Transaction transaction = Transaction.builder()
                .senderWallet(senderWallet)
                .receiverWallet(receiverWallet)
                .amount(new BigDecimal("250.0000"))
                .status(Status.COMPLETED)
                .build();
        Transaction savedTransaction = transactionRepositorie.save(transaction);

        // when
        Transaction foundTransaction = transactionRepositorie.findById(savedTransaction.getId()).orElse(null);

        // then
        assertThat(foundTransaction).isNotNull();
        assertThat(foundTransaction.getId()).isEqualTo(savedTransaction.getId());
        assertThat(foundTransaction.getAmount()).isEqualByComparingTo(new BigDecimal("250.0000"));
    }

    @Test
    void itShouldFindTransactionsBySenderWalletId() {
        // given
        Transaction transaction1 = Transaction.builder()
                .senderWallet(senderWallet)
                .receiverWallet(receiverWallet)
                .amount(new BigDecimal("100.0000"))
                .build();

        Transaction transaction2 = Transaction.builder()
                .senderWallet(senderWallet)
                .receiverWallet(receiverWallet)
                .amount(new BigDecimal("200.0000"))
                .build();

        transactionRepositorie.saveAll(List.of(transaction1, transaction2));

        // when
        List<Transaction> transactions = transactionRepositorie
                .findBySenderWalletIdOrReceiverWalletId(senderWallet.getId(), senderWallet.getId());

        // then
        assertThat(transactions).hasSize(2);
    }

    @Test
    void itShouldFindTransactionsByReceiverWalletId() {
        // given
        Transaction transaction = Transaction.builder()
                .senderWallet(senderWallet)
                .receiverWallet(receiverWallet)
                .amount(new BigDecimal("300.0000"))
                .build();
        transactionRepositorie.save(transaction);

        // when
        List<Transaction> transactions = transactionRepositorie
                .findBySenderWalletIdOrReceiverWalletId(receiverWallet.getId(), receiverWallet.getId());

        // then
        assertThat(transactions).hasSize(1);
        assertThat(transactions.get(0).getReceiverWallet().getId()).isEqualTo(receiverWallet.getId());
    }

    @Test
    void itShouldFindTransactionsBySenderOrReceiverWalletId() {
        // given
        Wallet thirdWallet = Wallet.builder()
                .user(testUser)
                .walletCurrency(WalletCurrency.EUR)
                .balance(new BigDecimal("2000.0000"))
                .build();
        walletRepository.save(thirdWallet);

        // Transaction where senderWallet is the sender
        Transaction transaction1 = Transaction.builder()
                .senderWallet(senderWallet)
                .receiverWallet(receiverWallet)
                .amount(new BigDecimal("100.0000"))
                .build();

        // Transaction where senderWallet is the receiver
        Transaction transaction2 = Transaction.builder()
                .senderWallet(thirdWallet)
                .receiverWallet(senderWallet)
                .amount(new BigDecimal("200.0000"))
                .build();

        // Transaction not involving senderWallet
        Transaction transaction3 = Transaction.builder()
                .senderWallet(thirdWallet)
                .receiverWallet(receiverWallet)
                .amount(new BigDecimal("50.0000"))
                .build();

        transactionRepositorie.saveAll(List.of(transaction1, transaction2, transaction3));

        // when
        List<Transaction> transactions = transactionRepositorie
                .findBySenderWalletIdOrReceiverWalletId(senderWallet.getId(), senderWallet.getId());

        // then
        assertThat(transactions).hasSize(2);
    }

    @Test
    void itShouldReturnEmptyListWhenNoTransactionsFound() {
        // given
        Wallet emptyWallet = Wallet.builder()
                .user(testUser)
                .walletCurrency(WalletCurrency.GBP)
                .balance(BigDecimal.ZERO)
                .build();
        walletRepository.save(emptyWallet);

        // when
        List<Transaction> transactions = transactionRepositorie
                .findBySenderWalletIdOrReceiverWalletId(emptyWallet.getId(), emptyWallet.getId());

        // then
        assertThat(transactions).isEmpty();
    }

    @Test
    void itShouldUpdateTransactionStatus() {
        // given
        Transaction transaction = Transaction.builder()
                .senderWallet(senderWallet)
                .receiverWallet(receiverWallet)
                .amount(new BigDecimal("500.0000"))
                .status(Status.PENDING)
                .build();
        Transaction savedTransaction = transactionRepositorie.save(transaction);

        // when
        savedTransaction.setStatus(Status.COMPLETED);
        Transaction updatedTransaction = transactionRepositorie.save(savedTransaction);

        // then
        assertThat(updatedTransaction.getStatus()).isEqualTo(Status.COMPLETED);
    }

    @Test
    void itShouldSetDefaultStatusToPending() {
        // given
        Transaction transaction = Transaction.builder()
                .senderWallet(senderWallet)
                .receiverWallet(receiverWallet)
                .amount(new BigDecimal("100.0000"))
                .build();

        // when
        Transaction savedTransaction = transactionRepositorie.save(transaction);

        // then
        assertThat(savedTransaction.getStatus()).isEqualTo(Status.PENDING);
    }

    @Test
    void itShouldSetCreatedAtAutomatically() {
        // given
        Transaction transaction = Transaction.builder()
                .senderWallet(senderWallet)
                .receiverWallet(receiverWallet)
                .amount(new BigDecimal("100.0000"))
                .build();

        // when
        Transaction savedTransaction = transactionRepositorie.save(transaction);

        // then
        assertThat(savedTransaction.getCreatedAt()).isNotNull();
    }

    @Test
    void itShouldDeleteTransaction() {
        // given
        Transaction transaction = Transaction.builder()
                .senderWallet(senderWallet)
                .receiverWallet(receiverWallet)
                .amount(new BigDecimal("100.0000"))
                .build();
        Transaction savedTransaction = transactionRepositorie.save(transaction);
        Long transactionId = savedTransaction.getId();

        // when
        transactionRepositorie.deleteById(transactionId);

        // then
        assertThat(transactionRepositorie.findById(transactionId)).isEmpty();
    }

    @Test
    void itShouldSaveTransactionWithDifferentStatuses() {
        // given
        Transaction pendingTx = Transaction.builder()
                .senderWallet(senderWallet)
                .receiverWallet(receiverWallet)
                .amount(new BigDecimal("100.0000"))
                .status(Status.PENDING)
                .build();

        Transaction completedTx = Transaction.builder()
                .senderWallet(senderWallet)
                .receiverWallet(receiverWallet)
                .amount(new BigDecimal("200.0000"))
                .status(Status.COMPLETED)
                .build();

        Transaction failedTx = Transaction.builder()
                .senderWallet(senderWallet)
                .receiverWallet(receiverWallet)
                .amount(new BigDecimal("300.0000"))
                .status(Status.FAILED)
                .build();

        Transaction cancelledTx = Transaction.builder()
                .senderWallet(senderWallet)
                .receiverWallet(receiverWallet)
                .amount(new BigDecimal("400.0000"))
                .status(Status.CANCELLED)
                .build();

        // when
        List<Transaction> savedTransactions = transactionRepositorie
                .saveAll(List.of(pendingTx, completedTx, failedTx, cancelledTx));

        // then
        assertThat(savedTransactions).hasSize(4);
        assertThat(savedTransactions).extracting(Transaction::getStatus)
                .containsExactlyInAnyOrder(Status.PENDING, Status.COMPLETED, Status.FAILED, Status.CANCELLED);
    }

    @Test
    void itShouldFindAllTransactions() {
        // given
        Transaction transaction1 = Transaction.builder()
                .senderWallet(senderWallet)
                .receiverWallet(receiverWallet)
                .amount(new BigDecimal("100.0000"))
                .build();

        Transaction transaction2 = Transaction.builder()
                .senderWallet(receiverWallet)
                .receiverWallet(senderWallet)
                .amount(new BigDecimal("50.0000"))
                .build();

        transactionRepositorie.saveAll(List.of(transaction1, transaction2));

        // when
        List<Transaction> allTransactions = transactionRepositorie.findAll();

        // then
        assertThat(allTransactions).hasSize(2);
    }

    @Test
    void itShouldFindTransactionsByStatus() {
        // given
        Transaction pendingTx = Transaction.builder()
                .senderWallet(senderWallet)
                .receiverWallet(receiverWallet)
                .amount(new BigDecimal("100.0000"))
                .status(Status.PENDING)
                .build();

        Transaction completedTx = Transaction.builder()
                .senderWallet(senderWallet)
                .receiverWallet(receiverWallet)
                .amount(new BigDecimal("200.0000"))
                .status(Status.COMPLETED)
                .build();

        Transaction anotherCompletedTx = Transaction.builder()
                .senderWallet(senderWallet)
                .receiverWallet(receiverWallet)
                .amount(new BigDecimal("300.0000"))
                .status(Status.COMPLETED)
                .build();

        transactionRepositorie.saveAll(List.of(pendingTx, completedTx, anotherCompletedTx));

        // when
        List<Transaction> completedTransactions = transactionRepositorie.findByStatus(Status.COMPLETED);

        // then
        assertThat(completedTransactions).hasSize(2);
        assertThat(completedTransactions).allMatch(t -> t.getStatus() == Status.COMPLETED);
    }

    @Test
    void itShouldFindTransactionsByCreatedAtBetween() {
        // given
        Transaction transaction1 = Transaction.builder()
                .senderWallet(senderWallet)
                .receiverWallet(receiverWallet)
                .amount(new BigDecimal("100.0000"))
                .build();
        Transaction transaction2 = Transaction.builder()
                .senderWallet(senderWallet)
                .receiverWallet(receiverWallet)
                .amount(new BigDecimal("200.0000"))
                .build();

        transactionRepositorie.saveAll(List.of(transaction1, transaction2));

        // when
        java.time.LocalDateTime startOfDay = java.time.LocalDate.now().atStartOfDay();
        java.time.LocalDateTime endOfDay = java.time.LocalDate.now().atTime(23, 59, 59);
        List<Transaction> todayTransactions = transactionRepositorie.findByCreatedAtBetween(startOfDay, endOfDay);

        // then
        assertThat(todayTransactions).hasSize(2);
    }

    @Test
    void itShouldReturnEmptyListWhenNoTransactionsInDateRange() {
        // given
        Transaction transaction = Transaction.builder()
                .senderWallet(senderWallet)
                .receiverWallet(receiverWallet)
                .amount(new BigDecimal("100.0000"))
                .build();
        transactionRepositorie.save(transaction);

        // when - search for transactions from last year
        java.time.LocalDateTime lastYearStart = java.time.LocalDate.now().minusYears(1).atStartOfDay();
        java.time.LocalDateTime lastYearEnd = java.time.LocalDate.now().minusYears(1).atTime(23, 59, 59);
        List<Transaction> transactions = transactionRepositorie.findByCreatedAtBetween(lastYearStart, lastYearEnd);

        // then
        assertThat(transactions).isEmpty();
    }

    @Test
    void itShouldReturnEmptyListWhenNoTransactionsWithStatus() {
        // given
        Transaction pendingTx = Transaction.builder()
                .senderWallet(senderWallet)
                .receiverWallet(receiverWallet)
                .amount(new BigDecimal("100.0000"))
                .status(Status.PENDING)
                .build();
        transactionRepositorie.save(pendingTx);

        // when
        List<Transaction> failedTransactions = transactionRepositorie.findByStatus(Status.FAILED);

        // then
        assertThat(failedTransactions).isEmpty();
    }
}