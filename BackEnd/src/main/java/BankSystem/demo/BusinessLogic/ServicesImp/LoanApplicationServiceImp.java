package BankSystem.demo.BusinessLogic.ServicesImp;

import BankSystem.demo.Aspect.Auditing.AuditLog;
import BankSystem.demo.Aspect.Preformance.PerformanceAspect;
import BankSystem.demo.Aspect.Security.OnlyForSameUser;
import BankSystem.demo.Aspect.Security.RequiresAdmin;
import BankSystem.demo.BusinessLogic.Services.LoanApplicationService;
import BankSystem.demo.Config.CurrentUserProvider;
import BankSystem.demo.DataAccessLayer.DTOs.LoanApplication.LoanApplicationRequestDTO;
import BankSystem.demo.DataAccessLayer.DTOs.LoanApplication.LoanApplicationResponseDTO;
import BankSystem.demo.DataAccessLayer.DTOs.LoanApplication.LoanApplicationUpdateDTO;
import BankSystem.demo.DataAccessLayer.Entites.LoanApplication;
import BankSystem.demo.DataAccessLayer.Entites.LoanStatus;
import BankSystem.demo.DataAccessLayer.Entites.User;
import BankSystem.demo.DataAccessLayer.Repositories.LoanApplicationRepository;
import BankSystem.demo.DataAccessLayer.Repositories.UserRepositorie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanApplicationServiceImp implements LoanApplicationService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final UserRepositorie userRepository;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @Transactional
    @PerformanceAspect
    @AuditLog
    @OnlyForSameUser
    public LoanApplicationResponseDTO createLoanApplication(LoanApplicationRequestDTO requestDTO) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("User not authenticated");
        }
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + currentUserId));

        // Convert DTO to Entity
        LoanApplication loanApplication = convertRequestDTOToEntity(requestDTO, user);

        // Save and return
        LoanApplication savedLoan = loanApplicationRepository.save(loanApplication);
        return convertEntityToResponseDTO(savedLoan);
    }

    @Override
    @OnlyForSameUser
    public LoanApplicationResponseDTO getLoanApplicationById(Long id) {
        LoanApplication loanApplication = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan application not found with ID: " + id));
        return convertEntityToResponseDTO(loanApplication);
    }

    @Override
    @PerformanceAspect
    @RequiresAdmin
    @AuditLog
    public List<LoanApplicationResponseDTO> getAllLoanApplications() {
        return loanApplicationRepository.findAll()
                .stream()
                .map(this::convertEntityToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @OnlyForSameUser
    public List<LoanApplicationResponseDTO> getLoanApplicationsByUserId(Long userId) {
        return loanApplicationRepository.findByUserId(userId)
                .stream()
                .map(this::convertEntityToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @RequiresAdmin
    public List<LoanApplicationResponseDTO> getLoanApplicationsByStatus(String status) {
        try {
            LoanStatus loanStatus = LoanStatus.valueOf(status.toUpperCase());
            return loanApplicationRepository.findByStatus(loanStatus)
                    .stream()
                    .map(this::convertEntityToResponseDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid loan status: " + status);
        }
    }

    @Override
    @Transactional
    @PerformanceAspect
    @RequiresAdmin
    @AuditLog
    public LoanApplicationResponseDTO updateLoanApplicationStatus(LoanApplicationUpdateDTO updateDTO) {
        LoanApplication loanApplication = loanApplicationRepository.findById(updateDTO.getId())
                .orElseThrow(() -> new RuntimeException("Loan application not found with ID: " + updateDTO.getId()));

        try {
            LoanStatus newStatus = LoanStatus.valueOf(updateDTO.getStatus().toUpperCase());
            loanApplication.setStatus(newStatus);
            LoanApplication updatedLoan = loanApplicationRepository.save(loanApplication);
            return convertEntityToResponseDTO(updatedLoan);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid loan status: " + updateDTO.getStatus());
        }
    }

    @Override
    @Transactional
    @RequiresAdmin
    @OnlyForSameUser
    @AuditLog
    public void deleteLoanApplication(Long id) {
        if (!loanApplicationRepository.existsById(id)) {
            throw new RuntimeException("Loan application not found with ID: " + id);
        }
        loanApplicationRepository.deleteById(id);
    }

    @Override
    public boolean existsByUserIdAndStatus(Long userId, String status) {
        try {
            LoanStatus loanStatus = LoanStatus.valueOf(status.toUpperCase());
            return loanApplicationRepository.existsByUserIdAndStatus(userId, loanStatus);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid loan status: " + status);
        }
    }

    @Override
    @RequiresAdmin
    public long countByStatus(String status) {
        try {
            LoanStatus loanStatus = LoanStatus.valueOf(status.toUpperCase());
            return loanApplicationRepository.countByStatus(loanStatus);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid loan status: " + status);
        }
    }

    // Conversion methods
    private LoanApplication convertRequestDTOToEntity(LoanApplicationRequestDTO dto, User user) {
        return LoanApplication.builder()
                .user(user)
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .dateOfBirth(dto.getDateOfBirth())
                .maritalStatus(dto.getMaritalStatus())
                .employmentStatus(dto.getEmploymentStatus())
                .monthlyIncome(dto.getMonthlyIncome())
                .employerName(dto.getEmployerName())
                .yearsAtCurrentJob(dto.getYearsAtCurrentJob())
                .loanPurpose(dto.getLoanPurpose())
                .requestedAmount(dto.getRequestedAmount())
                .status(LoanStatus.PENDING)
                .build();
    }

    private LoanApplicationResponseDTO convertEntityToResponseDTO(LoanApplication entity) {
        return LoanApplicationResponseDTO.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .fullName(entity.getFullName())
                .email(entity.getEmail())
                .phoneNumber(entity.getPhoneNumber())
                .dateOfBirth(entity.getDateOfBirth())
                .maritalStatus(entity.getMaritalStatus())
                .employmentStatus(entity.getEmploymentStatus())
                .monthlyIncome(entity.getMonthlyIncome())
                .employerName(entity.getEmployerName())
                .yearsAtCurrentJob(entity.getYearsAtCurrentJob())
                .loanPurpose(entity.getLoanPurpose())
                .requestedAmount(entity.getRequestedAmount())
                .status(entity.getStatus().name())
                .appliedAt(entity.getAppliedAt())
                .build();
    }
}
