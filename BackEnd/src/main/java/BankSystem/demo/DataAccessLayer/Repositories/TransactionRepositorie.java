package BankSystem.demo.DataAccessLayer.Repositories;

import BankSystem.demo.DataAccessLayer.Entites.Status;
import BankSystem.demo.DataAccessLayer.Entites.Transaction;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepositorie extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySenderWalletIdOrReceiverWalletId(Long senderWalletId, Long receiverWalletId);

    List<Transaction> findByStatus(Status status);

    List<Transaction> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<Transaction> findBySenderWalletIdOrReceiverWalletIdAndCreatedAtBetween(
            Long senderWalletId, Long receiverWalletId, LocalDateTime start, LocalDateTime end);


    @Query("SELECT t FROM Transaction t " +
            "WHERE t.senderWallet.user.id = :userId " +
            "OR t.receiverWallet.user.id = :userId")
    List<Transaction> findAllTransactionsRelatedToUser(@Param("userId") Long userId);
}
