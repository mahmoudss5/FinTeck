package BankSystem.demo.DataAccessLayer.DTOs.Transaction;

import BankSystem.demo.DataAccessLayer.Entites.WalletCurrency;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequestDTO {

    @NotNull(message = "Sender wallet ID is required")
    private Long senderWalletId;


    @NotNull(message = "Reciver name is required")
    private String receiverUserName;

    @NotNull(message = "Currency is required")
    private WalletCurrency currency;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0001", message = "Amount must be greater than 0")
    private BigDecimal amount;
}
