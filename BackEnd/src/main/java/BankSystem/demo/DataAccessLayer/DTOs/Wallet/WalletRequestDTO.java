package BankSystem.demo.DataAccessLayer.DTOs.Wallet;

import BankSystem.demo.DataAccessLayer.Entites.WalletCurrency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletRequestDTO {

    @Builder.Default
    private WalletCurrency currency = WalletCurrency.USD;

    @Builder.Default
    private BigDecimal initialBalance = BigDecimal.ZERO;

    private Boolean active;
}
