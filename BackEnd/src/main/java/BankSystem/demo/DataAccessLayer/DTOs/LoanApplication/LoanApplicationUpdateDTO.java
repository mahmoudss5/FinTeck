package BankSystem.demo.DataAccessLayer.DTOs.LoanApplication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApplicationUpdateDTO {

    @NotNull(message = "Loan application ID is required")
    private Long id;

    @NotBlank(message = "Status is required")
    private String status;
}
