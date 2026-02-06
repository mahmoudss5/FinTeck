package BankSystem.demo.DataAccessLayer.Repositories;

import BankSystem.demo.DataAccessLayer.Entites.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepositorie extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String username);
    Boolean existsByUserName(String username);
    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    // to solve the n+1 problem when fetching user with wallets
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.wallets WHERE u.email = :email")
    Optional<User> findByEmailWithWallets(@Param("email") String email);
}
