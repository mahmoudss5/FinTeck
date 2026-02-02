package BankSystem.demo.DataAccessLayer.Repositories;

import BankSystem.demo.DataAccessLayer.Entites.Wallet;
import BankSystem.demo.DataAccessLayer.Entites.WalletCurrency;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    List<Wallet> findByUserUserName(String username);
    Wallet findByWalletCurrencyAndUserUserName(WalletCurrency currency, String username);
    List<Wallet> findByWalletCurrency(WalletCurrency currency);
    Boolean existsByUserIdAndWalletCurrency(Long userId, WalletCurrency currency);
    List<Wallet> findByUserId(Long userId);
}