package BankSystem.demo.DataAccessLayer.Repositories;

import BankSystem.demo.DataAccessLayer.Entites.LoanApplication;
import BankSystem.demo.DataAccessLayer.Entites.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {

    List<LoanApplication> findByUserId(Long userId);

    List<LoanApplication> findByStatus(LoanStatus status);

    List<LoanApplication> findByUserIdAndStatus(Long userId, LoanStatus status);

    boolean existsByUserIdAndStatus(Long userId, LoanStatus status);

    long countByStatus(LoanStatus status);

    List<LoanApplication> findByEmailContainingIgnoreCase(String email);
}
