package BankSystem.demo.DataAccessLayer.DTOs.LoanApplication;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApplicationResponseDTO {

    private Long id;
    private Long userId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String maritalStatus;
    private String employmentStatus;
    private BigDecimal monthlyIncome;
    private String employerName;
    private String yearsAtCurrentJob;
    private String loanPurpose;
    private BigDecimal requestedAmount;
    private String status;
    private LocalDateTime appliedAt;
}
