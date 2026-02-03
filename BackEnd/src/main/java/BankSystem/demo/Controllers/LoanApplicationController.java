package BankSystem.demo.Controllers;
import BankSystem.demo.BusinessLogic.Services.LoanApplicationService;
import BankSystem.demo.DataAccessLayer.DTOs.LoanApplication.LoanApplicationRequestDTO;
import BankSystem.demo.DataAccessLayer.DTOs.LoanApplication.LoanApplicationResponseDTO;
import BankSystem.demo.DataAccessLayer.DTOs.LoanApplication.LoanApplicationUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Loan Application Controller", description = "APIs for managing loan applications")
@RestController
@RequestMapping("/loan-applications/api")
@RequiredArgsConstructor
public class LoanApplicationController {

    private final LoanApplicationService loanApplicationService;

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create Loan Application", description = "Submit a new loan application")
    @PostMapping
    public ResponseEntity<LoanApplicationResponseDTO> createLoanApplication(
            @RequestBody @Valid LoanApplicationRequestDTO requestDTO) {
        LoanApplicationResponseDTO response = loanApplicationService.createLoanApplication(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get Loan Application by ID", description = "Retrieve a specific loan application by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<LoanApplicationResponseDTO> getLoanApplicationById(@PathVariable Long id) {
        LoanApplicationResponseDTO response = loanApplicationService.getLoanApplicationById(id);
        return ResponseEntity.ok(response);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get All Loan Applications", description = "Retrieve all loan applications")
    @GetMapping("/all")
    public ResponseEntity<List<LoanApplicationResponseDTO>> getAllLoanApplications() {
        List<LoanApplicationResponseDTO> response = loanApplicationService.getAllLoanApplications();
        return ResponseEntity.ok(response);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get Loan Applications by User ID", description = "Retrieve all loan applications for a specific user")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LoanApplicationResponseDTO>> getLoanApplicationsByUserId(@PathVariable Long userId) {
        List<LoanApplicationResponseDTO> response = loanApplicationService.getLoanApplicationsByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get Loan Applications by Status", description = "Retrieve all loan applications with a specific status")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<LoanApplicationResponseDTO>> getLoanApplicationsByStatus(@PathVariable String status) {
        List<LoanApplicationResponseDTO> response = loanApplicationService.getLoanApplicationsByStatus(status);
        return ResponseEntity.ok(response);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update Loan Application Status", description = "Update the status of a loan application")
    @PatchMapping("/status")
    public ResponseEntity<LoanApplicationResponseDTO> updateLoanApplicationStatus(
            @RequestBody @Valid LoanApplicationUpdateDTO updateDTO) {
        LoanApplicationResponseDTO response = loanApplicationService.updateLoanApplicationStatus(updateDTO);
        return ResponseEntity.ok(response);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete Loan Application", description = "Delete a loan application by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoanApplication(@PathVariable Long id) {
        loanApplicationService.deleteLoanApplication(id);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Check Loan Application Exists", description = "Check if a user has a loan application with a specific status")
    @GetMapping("/exists/{userId}/{status}")
    public ResponseEntity<Boolean> existsByUserIdAndStatus(
            @PathVariable Long userId,
            @PathVariable String status) {
        boolean exists = loanApplicationService.existsByUserIdAndStatus(userId, status);
        return ResponseEntity.ok(exists);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Count Loan Applications by Status", description = "Get the count of loan applications by status")
    @GetMapping("/count/{status}")
    public ResponseEntity<Long> countByStatus(@PathVariable String status) {
        long count = loanApplicationService.countByStatus(status);
        return ResponseEntity.ok(count);
    }
}
