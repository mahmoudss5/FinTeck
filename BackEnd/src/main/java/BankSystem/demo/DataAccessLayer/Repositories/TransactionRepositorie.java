package BankSystem.demo.DataAccessLayer.Repositories;

import BankSystem.demo.DataAccessLayer.Entites.Status;
import BankSystem.demo.DataAccessLayer.Entites.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
