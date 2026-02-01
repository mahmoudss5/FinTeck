package BankSystem.demo.DataAccessLayer.DTOs.Transaction;

import BankSystem.demo.DataAccessLayer.Entites.Status;
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
public class TransactionResponseDTO {

    private Long senderWalletId;

    private String senderUserName;

    private String receiverUserName;

    private WalletCurrency currency;
    private BigDecimal amount;
    private Status status;

    private LocalDateTime createdAt;
}
