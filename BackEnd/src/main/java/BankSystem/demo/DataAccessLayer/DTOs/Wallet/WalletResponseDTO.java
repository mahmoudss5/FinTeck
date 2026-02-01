package BankSystem.demo.DataAccessLayer.DTOs.Wallet;

import BankSystem.demo.DataAccessLayer.Entites.WalletCurrency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletResponseDTO {

    private Long id;
    private Long userId;
    private String userName;
    private WalletCurrency currency;
    private BigDecimal balance;
    private Boolean active;
    private LocalDateTime createdAt;
}
