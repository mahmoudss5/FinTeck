package BankSystem.demo.DataAccessLayer.Repositories;

import BankSystem.demo.DataAccessLayer.Entites.User;
import BankSystem.demo.DataAccessLayer.Entites.Wallet;
import BankSystem.demo.DataAccessLayer.Entites.WalletCurrency;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
class WalletRepositoryTest {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepositorie userRepositorie;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .userName("test_user")
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .password("hashed_password")
                .build();
        userRepositorie.save(testUser);
    }

    @AfterEach
    void tearDown() {
        walletRepository.deleteAll();
        userRepositorie.deleteAll();
    }

    @Test
    void itShouldSaveWallet() {
        // given
        Wallet wallet = Wallet.builder()
                .user(testUser)
                .walletCurrency(WalletCurrency.USD)
                .balance(new BigDecimal("1000.0000"))
                .active(true)
                .build();

        // when
        Wallet savedWallet = walletRepository.save(wallet);

        // then
        assertThat(savedWallet).isNotNull();
        assertThat(savedWallet.getId()).isNotNull();
        assertThat(savedWallet.getBalance()).isEqualByComparingTo(new BigDecimal("1000.0000"));
        assertThat(savedWallet.getWalletCurrency()).isEqualTo(WalletCurrency.USD);
        assertThat(savedWallet.getActive()).isTrue();
    }

    @Test
    void itShouldFindWalletById() {
        // given
        Wallet wallet = Wallet.builder()
                .user(testUser)
                .walletCurrency(WalletCurrency.EUR)
                .balance(new BigDecimal("500.0000"))
                .build();
        Wallet savedWallet = walletRepository.save(wallet);

        // when
        Wallet foundWallet = walletRepository.findById(savedWallet.getId()).orElse(null);

        // then
        assertThat(foundWallet).isNotNull();
        assertThat(foundWallet.getId()).isEqualTo(savedWallet.getId());
        assertThat(foundWallet.getWalletCurrency()).isEqualTo(WalletCurrency.EUR);
    }

    @Test
    void itShouldFindWalletsByUserUserName() {
        // given
        Wallet wallet1 = Wallet.builder()
                .user(testUser)
                .walletCurrency(WalletCurrency.USD)
                .balance(new BigDecimal("1000.0000"))
                .build();

        Wallet wallet2 = Wallet.builder()
                .user(testUser)
                .walletCurrency(WalletCurrency.EUR)
                .balance(new BigDecimal("2000.0000"))
                .build();

        walletRepository.saveAll(List.of(wallet1, wallet2));

        // when
        List<Wallet> wallets = walletRepository.findByUserUserName("test_user");

        // then
        assertThat(wallets).hasSize(2);
        assertThat(wallets).extracting(Wallet::getWalletCurrency)
                .containsExactlyInAnyOrder(WalletCurrency.USD, WalletCurrency.EUR);
    }

    @Test
    void itShouldReturnEmptyListWhenUserNameNotFound() {
        // given
        Wallet wallet = Wallet.builder()
                .user(testUser)
                .walletCurrency(WalletCurrency.USD)
                .build();
        walletRepository.save(wallet);

        // when
        List<Wallet> wallets = walletRepository.findByUserUserName("non_existent_user");

        // then
        assertThat(wallets).isEmpty();
    }

    @Test
    void itShouldFindWalletsByWalletCurrency() {
        // given
        User anotherUser = User.builder()
                .userName("another_user")
                .email("another@example.com")
                .firstName("Another")
                .lastName("User")
                .password("hashed_password")
                .build();
        userRepositorie.save(anotherUser);

        Wallet wallet1 = Wallet.builder()
                .user(testUser)
                .walletCurrency(WalletCurrency.USD)
                .balance(new BigDecimal("1000.0000"))
                .build();

        Wallet wallet2 = Wallet.builder()
                .user(anotherUser)
                .walletCurrency(WalletCurrency.USD)
                .balance(new BigDecimal("2000.0000"))
                .build();

        Wallet wallet3 = Wallet.builder()
                .user(testUser)
                .walletCurrency(WalletCurrency.EUR)
                .balance(new BigDecimal("500.0000"))
                .build();

        walletRepository.saveAll(List.of(wallet1, wallet2, wallet3));

        // when
        List<Wallet> usdWallets = walletRepository.findByWalletCurrency(WalletCurrency.USD);

        // then
        assertThat(usdWallets).hasSize(2);
        assertThat(usdWallets).allMatch(w -> w.getWalletCurrency() == WalletCurrency.USD);
    }

    @Test
    void itShouldReturnEmptyListWhenCurrencyNotFound() {
        // given
        Wallet wallet = Wallet.builder()
                .user(testUser)
                .walletCurrency(WalletCurrency.USD)
                .build();
        walletRepository.save(wallet);

        // when
        List<Wallet> wallets = walletRepository.findByWalletCurrency(WalletCurrency.GBP);

        // then
        assertThat(wallets).isEmpty();
    }

    @Test
    void itShouldUpdateWalletBalance() {
        // given
        Wallet wallet = Wallet.builder()
                .user(testUser)
                .walletCurrency(WalletCurrency.USD)
                .balance(new BigDecimal("1000.0000"))
                .build();
        Wallet savedWallet = walletRepository.save(wallet);

        // when
        savedWallet.setBalance(new BigDecimal("1500.0000"));
        Wallet updatedWallet = walletRepository.save(savedWallet);

        // then
        assertThat(updatedWallet.getBalance()).isEqualByComparingTo(new BigDecimal("1500.0000"));
    }

    @Test
    void itShouldDeactivateWallet() {
        // given
        Wallet wallet = Wallet.builder()
                .user(testUser)
                .walletCurrency(WalletCurrency.USD)
                .active(true)
                .build();
        Wallet savedWallet = walletRepository.save(wallet);

        // when
        savedWallet.setActive(false);
        Wallet updatedWallet = walletRepository.save(savedWallet);

        // then
        assertThat(updatedWallet.getActive()).isFalse();
    }

    @Test
    void itShouldDeleteWallet() {
        // given
        Wallet wallet = Wallet.builder()
                .user(testUser)
                .walletCurrency(WalletCurrency.USD)
                .build();
        Wallet savedWallet = walletRepository.save(wallet);
        Long walletId = savedWallet.getId();

        // when
        walletRepository.deleteById(walletId);

        // then
        assertThat(walletRepository.findById(walletId)).isEmpty();
    }

    @Test
    void itShouldSetDefaultValues() {
        // given
        Wallet wallet = Wallet.builder()
                .user(testUser)
                .walletCurrency(WalletCurrency.USD)
                .build();

        // when
        Wallet savedWallet = walletRepository.save(wallet);

        // then
        assertThat(savedWallet.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(savedWallet.getActive()).isTrue();
        assertThat(savedWallet.getCreatedAt()).isNotNull();
    }

    @Test
    void itShouldFindWalletByWalletCurrencyAndUserUserName() {
        // given
        Wallet usdWallet = Wallet.builder()
                .user(testUser)
                .walletCurrency(WalletCurrency.USD)
                .balance(new BigDecimal("1000.0000"))
                .build();
        Wallet eurWallet = Wallet.builder()
                .user(testUser)
                .walletCurrency(WalletCurrency.EUR)
                .balance(new BigDecimal("500.0000"))
                .build();
        walletRepository.saveAll(List.of(usdWallet, eurWallet));

        // when
        Wallet foundWallet = walletRepository.findByWalletCurrencyAndUserUserName(WalletCurrency.USD, "test_user");

        // then
        assertThat(foundWallet).isNotNull();
        assertThat(foundWallet.getWalletCurrency()).isEqualTo(WalletCurrency.USD);
        assertThat(foundWallet.getBalance()).isEqualByComparingTo(new BigDecimal("1000.0000"));
    }

    @Test
    void itShouldReturnNullWhenWalletCurrencyAndUserNameNotFound() {
        // given
        Wallet wallet = Wallet.builder()
                .user(testUser)
                .walletCurrency(WalletCurrency.USD)
                .build();
        walletRepository.save(wallet);

        // when
        Wallet foundWallet = walletRepository.findByWalletCurrencyAndUserUserName(WalletCurrency.EUR, "test_user");

        // then
        assertThat(foundWallet).isNull();
    }

    @Test
    void itShouldCheckIfWalletExistsByUserIdAndCurrency() {
        // given
        Wallet wallet = Wallet.builder()
                .user(testUser)
                .walletCurrency(WalletCurrency.USD)
                .build();
        walletRepository.save(wallet);

        // when
        Boolean exists = walletRepository.existsByUserIdAndWalletCurrency(testUser.getId(), WalletCurrency.USD);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void itShouldReturnFalseWhenWalletDoesNotExistByUserIdAndCurrency() {
        // given
        Wallet wallet = Wallet.builder()
                .user(testUser)
                .walletCurrency(WalletCurrency.USD)
                .build();
        walletRepository.save(wallet);

        // when
        Boolean exists = walletRepository.existsByUserIdAndWalletCurrency(testUser.getId(), WalletCurrency.GBP);

        // then
        assertThat(exists).isFalse();
    }

    @Test
    void itShouldPreventDuplicateWalletForSameUserAndCurrency() {
        // given
        Wallet wallet1 = Wallet.builder()
                .user(testUser)
                .walletCurrency(WalletCurrency.USD)
                .build();
        walletRepository.save(wallet1);

        // when - check existence before creating another
        Boolean alreadyExists = walletRepository.existsByUserIdAndWalletCurrency(testUser.getId(), WalletCurrency.USD);

        // then
        assertThat(alreadyExists).isTrue();
    }

    @Test
    void itShouldAllowSameUserToHaveMultipleCurrencyWallets() {
        // given
        Wallet usdWallet = Wallet.builder()
                .user(testUser)
                .walletCurrency(WalletCurrency.USD)
                .balance(new BigDecimal("1000.0000"))
                .build();
        Wallet eurWallet = Wallet.builder()
                .user(testUser)
                .walletCurrency(WalletCurrency.EUR)
                .balance(new BigDecimal("500.0000"))
                .build();
        Wallet gbpWallet = Wallet.builder()
                .user(testUser)
                .walletCurrency(WalletCurrency.GBP)
                .balance(new BigDecimal("750.0000"))
                .build();
        walletRepository.saveAll(List.of(usdWallet, eurWallet, gbpWallet));

        // when
        List<Wallet> userWallets = walletRepository.findByUserUserName("test_user");

        // then
        assertThat(userWallets).hasSize(3);
        assertThat(userWallets).extracting(Wallet::getWalletCurrency)
                .containsExactlyInAnyOrder(WalletCurrency.USD, WalletCurrency.EUR, WalletCurrency.GBP);
    }
}