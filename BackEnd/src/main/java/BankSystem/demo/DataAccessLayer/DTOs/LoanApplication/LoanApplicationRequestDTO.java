package BankSystem.demo.DataAccessLayer.DTOs.LoanApplication;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApplicationRequestDTO {

    @NotBlank(message = "Full name is required")
    @Size(max = 255, message = "Full name must not exceed 255 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Size(max = 50, message = "Phone number must not exceed 50 characters")
    @Pattern(regexp = "^[0-9+\\-() ]+$", message = "Phone number must be valid")
    private String phoneNumber;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Marital status is required")
    @Size(max = 50, message = "Marital status must not exceed 50 characters")
    private String maritalStatus;

    @NotBlank(message = "Employment status is required")
    @Size(max = 50, message = "Employment status must not exceed 50 characters")
    private String employmentStatus;

    @NotNull(message = "Monthly income is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Monthly income must be greater than 0")
    private BigDecimal monthlyIncome;

    @Size(max = 255, message = "Employer name must not exceed 255 characters")
    private String employerName;

    @Size(max = 50, message = "Years at current job must not exceed 50 characters")
    private String yearsAtCurrentJob;

    @NotBlank(message = "Loan purpose is required")
    @Size(max = 50, message = "Loan purpose must not exceed 50 characters")
    private String loanPurpose;

    @NotNull(message = "Requested amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Requested amount must be greater than 0")
    private BigDecimal requestedAmount;
}
