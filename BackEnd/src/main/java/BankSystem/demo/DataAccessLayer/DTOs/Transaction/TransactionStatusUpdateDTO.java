package BankSystem.demo.DataAccessLayer.DTOs.Transaction;

import BankSystem.demo.DataAccessLayer.Entites.Status;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionStatusUpdateDTO {

    @NotNull(message = "Status is required")
    private Status status;
}
