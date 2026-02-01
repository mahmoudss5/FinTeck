package BankSystem.demo.BusinessLogic.Services;

import BankSystem.demo.DataAccessLayer.DTOs.LoanApplication.LoanApplicationRequestDTO;
import BankSystem.demo.DataAccessLayer.DTOs.LoanApplication.LoanApplicationResponseDTO;
import BankSystem.demo.DataAccessLayer.DTOs.LoanApplication.LoanApplicationUpdateDTO;

import java.util.List;

public interface LoanApplicationService {

    LoanApplicationResponseDTO createLoanApplication(LoanApplicationRequestDTO requestDTO);

    LoanApplicationResponseDTO getLoanApplicationById(Long id);

    List<LoanApplicationResponseDTO> getAllLoanApplications();

    List<LoanApplicationResponseDTO> getLoanApplicationsByUserId(Long userId);

    List<LoanApplicationResponseDTO> getLoanApplicationsByStatus(String status);

    LoanApplicationResponseDTO updateLoanApplicationStatus(LoanApplicationUpdateDTO updateDTO);

    void deleteLoanApplication(Long id);

    boolean existsByUserIdAndStatus(Long userId, String status);

    long countByStatus(String status);
}
