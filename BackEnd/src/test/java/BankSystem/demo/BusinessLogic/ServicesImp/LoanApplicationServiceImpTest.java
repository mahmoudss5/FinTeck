package BankSystem.demo.BusinessLogic.ServicesImp;

import BankSystem.demo.Config.CurrentUserProvider;
import BankSystem.demo.DataAccessLayer.DTOs.LoanApplication.LoanApplicationRequestDTO;
import BankSystem.demo.DataAccessLayer.DTOs.LoanApplication.LoanApplicationResponseDTO;
import BankSystem.demo.DataAccessLayer.DTOs.LoanApplication.LoanApplicationUpdateDTO;
import BankSystem.demo.DataAccessLayer.Entites.LoanApplication;
import BankSystem.demo.DataAccessLayer.Entites.LoanStatus;
import BankSystem.demo.DataAccessLayer.Entites.User;
import BankSystem.demo.DataAccessLayer.Repositories.LoanApplicationRepository;
import BankSystem.demo.DataAccessLayer.Repositories.UserRepositorie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanApplicationServiceImpTest {

    @Mock
    private LoanApplicationRepository loanApplicationRepository;

    @Mock
    private UserRepositorie userRepository;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private LoanApplicationServiceImp loanApplicationService;

    private User testUser;
    private LoanApplication testLoanApplication;
    private LoanApplicationRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .userName("testuser")
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .build();

        testLoanApplication = LoanApplication.builder()
                .id(1L)
                .user(testUser)
                .fullName("Test User")
                .email("test@example.com")
                .phoneNumber("+1234567890")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .maritalStatus("Single")
                .employmentStatus("Employed")
                .monthlyIncome(new BigDecimal("5000.00"))
                .employerName("Test Company")
                .yearsAtCurrentJob("3")
                .loanPurpose("Personal")
                .requestedAmount(new BigDecimal("10000.00"))
                .status(LoanStatus.PENDING)
                .appliedAt(LocalDateTime.now())
                .build();

        requestDTO = LoanApplicationRequestDTO.builder()
                .fullName("Test User")
                .email("test@example.com")
                .phoneNumber("+1234567890")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .maritalStatus("Single")
                .employmentStatus("Employed")
                .monthlyIncome(new BigDecimal("5000.00"))
                .employerName("Test Company")
                .yearsAtCurrentJob("3")
                .loanPurpose("Personal")
                .requestedAmount(new BigDecimal("10000.00"))
                .build();
    }

    // ==================== CREATE LOAN APPLICATION TESTS ====================

    @Test
    void testCreateLoanApplication_Success() {
        // Arrange
        when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(loanApplicationRepository.save(any(LoanApplication.class))).thenReturn(testLoanApplication);

        // Act
        LoanApplicationResponseDTO result = loanApplicationService.createLoanApplication(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test User", result.getFullName());
        assertEquals("test@example.com", result.getEmail());
        assertEquals(new BigDecimal("10000.00"), result.getRequestedAmount());
        assertEquals("PENDING", result.getStatus());
        verify(userRepository, times(1)).findById(1L);
        verify(loanApplicationRepository, times(1)).save(any(LoanApplication.class));
    }

    @Test
    void testCreateLoanApplication_UserNotFound() {
        // Arrange
        when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> loanApplicationService.createLoanApplication(requestDTO));

        assertEquals("User not found with ID: 1", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
        verify(loanApplicationRepository, never()).save(any(LoanApplication.class));
    }

    // ==================== GET LOAN APPLICATION BY ID TESTS ====================

    @Test
    void testGetLoanApplicationById_Success() {
        // Arrange
        when(loanApplicationRepository.findById(1L)).thenReturn(Optional.of(testLoanApplication));

        // Act
        LoanApplicationResponseDTO result = loanApplicationService.getLoanApplicationById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test User", result.getFullName());
        assertEquals("PENDING", result.getStatus());
        verify(loanApplicationRepository, times(1)).findById(1L);
    }

    @Test
    void testGetLoanApplicationById_NotFound() {
        // Arrange
        when(loanApplicationRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> loanApplicationService.getLoanApplicationById(999L));

        assertEquals("Loan application not found with ID: 999", exception.getMessage());
        verify(loanApplicationRepository, times(1)).findById(999L);
    }

    // ==================== GET ALL LOAN APPLICATIONS TESTS ====================

    @Test
    void testGetAllLoanApplications_Success() {
        // Arrange
        LoanApplication loan2 = LoanApplication.builder()
                .id(2L)
                .user(testUser)
                .fullName("Another User")
                .email("another@example.com")
                .phoneNumber("+9876543210")
                .dateOfBirth(LocalDate.of(1985, 5, 15))
                .maritalStatus("Married")
                .employmentStatus("Self-Employed")
                .monthlyIncome(new BigDecimal("6000.00"))
                .loanPurpose("Business")
                .requestedAmount(new BigDecimal("20000.00"))
                .status(LoanStatus.APPROVED)
                .appliedAt(LocalDateTime.now())
                .build();

        when(loanApplicationRepository.findAll()).thenReturn(Arrays.asList(testLoanApplication, loan2));

        // Act
        List<LoanApplicationResponseDTO> result = loanApplicationService.getAllLoanApplications();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test User", result.get(0).getFullName());
        assertEquals("Another User", result.get(1).getFullName());
        verify(loanApplicationRepository, times(1)).findAll();
    }

    @Test
    void testGetAllLoanApplications_EmptyList() {
        // Arrange
        when(loanApplicationRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<LoanApplicationResponseDTO> result = loanApplicationService.getAllLoanApplications();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(loanApplicationRepository, times(1)).findAll();
    }

    // ==================== GET LOAN APPLICATIONS BY USER ID TESTS ====================

    @Test
    void testGetLoanApplicationsByUserId_Success() {
        // Arrange
        when(loanApplicationRepository.findByUserId(1L))
                .thenReturn(Arrays.asList(testLoanApplication));

        // Act
        List<LoanApplicationResponseDTO> result = loanApplicationService.getLoanApplicationsByUserId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUserId());
        verify(loanApplicationRepository, times(1)).findByUserId(1L);
    }

    @Test
    void testGetLoanApplicationsByUserId_EmptyList() {
        // Arrange
        when(loanApplicationRepository.findByUserId(999L)).thenReturn(Arrays.asList());

        // Act
        List<LoanApplicationResponseDTO> result = loanApplicationService.getLoanApplicationsByUserId(999L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(loanApplicationRepository, times(1)).findByUserId(999L);
    }

    // ==================== GET LOAN APPLICATIONS BY STATUS TESTS ====================

    @Test
    void testGetLoanApplicationsByStatus_Success() {
        // Arrange
        when(loanApplicationRepository.findByStatus(LoanStatus.PENDING))
                .thenReturn(Arrays.asList(testLoanApplication));

        // Act
        List<LoanApplicationResponseDTO> result = loanApplicationService.getLoanApplicationsByStatus("PENDING");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("PENDING", result.get(0).getStatus());
        verify(loanApplicationRepository, times(1)).findByStatus(LoanStatus.PENDING);
    }

    @Test
    void testGetLoanApplicationsByStatus_InvalidStatus() {
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> loanApplicationService.getLoanApplicationsByStatus("INVALID_STATUS"));

        assertEquals("Invalid loan status: INVALID_STATUS", exception.getMessage());
        verify(loanApplicationRepository, never()).findByStatus(any());
    }

    // ==================== UPDATE LOAN APPLICATION STATUS TESTS ====================

    @Test
    void testUpdateLoanApplicationStatus_Success() {
        // Arrange
        LoanApplicationUpdateDTO updateDTO = LoanApplicationUpdateDTO.builder()
                .id(1L)
                .status("APPROVED")
                .build();

        when(loanApplicationRepository.findById(1L)).thenReturn(Optional.of(testLoanApplication));
        when(loanApplicationRepository.save(any(LoanApplication.class))).thenAnswer(invocation -> {
            LoanApplication loan = invocation.getArgument(0);
            loan.setStatus(LoanStatus.APPROVED);
            return loan;
        });

        // Act
        LoanApplicationResponseDTO result = loanApplicationService.updateLoanApplicationStatus(updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals("APPROVED", result.getStatus());
        verify(loanApplicationRepository, times(1)).findById(1L);
        verify(loanApplicationRepository, times(1)).save(any(LoanApplication.class));
    }

    @Test
    void testUpdateLoanApplicationStatus_NotFound() {
        // Arrange
        LoanApplicationUpdateDTO updateDTO = LoanApplicationUpdateDTO.builder()
                .id(999L)
                .status("APPROVED")
                .build();

        when(loanApplicationRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> loanApplicationService.updateLoanApplicationStatus(updateDTO));

        assertEquals("Loan application not found with ID: 999", exception.getMessage());
        verify(loanApplicationRepository, times(1)).findById(999L);
        verify(loanApplicationRepository, never()).save(any(LoanApplication.class));
    }

    @Test
    void testUpdateLoanApplicationStatus_InvalidStatus() {
        // Arrange
        LoanApplicationUpdateDTO updateDTO = LoanApplicationUpdateDTO.builder()
                .id(1L)
                .status("INVALID_STATUS")
                .build();

        when(loanApplicationRepository.findById(1L)).thenReturn(Optional.of(testLoanApplication));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> loanApplicationService.updateLoanApplicationStatus(updateDTO));

        assertEquals("Invalid loan status: INVALID_STATUS", exception.getMessage());
        verify(loanApplicationRepository, times(1)).findById(1L);
        verify(loanApplicationRepository, never()).save(any(LoanApplication.class));
    }

    // ==================== DELETE LOAN APPLICATION TESTS ====================

    @Test
    void testDeleteLoanApplication_Success() {
        // Arrange
        when(loanApplicationRepository.existsById(1L)).thenReturn(true);
        doNothing().when(loanApplicationRepository).deleteById(1L);

        // Act
        loanApplicationService.deleteLoanApplication(1L);

        // Assert
        verify(loanApplicationRepository, times(1)).existsById(1L);
        verify(loanApplicationRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteLoanApplication_NotFound() {
        // Arrange
        when(loanApplicationRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> loanApplicationService.deleteLoanApplication(999L));

        assertEquals("Loan application not found with ID: 999", exception.getMessage());
        verify(loanApplicationRepository, times(1)).existsById(999L);
        verify(loanApplicationRepository, never()).deleteById(anyLong());
    }

    // ==================== EXISTS BY USER ID AND STATUS TESTS ====================

    @Test
    void testExistsByUserIdAndStatus_True() {
        // Arrange
        when(loanApplicationRepository.existsByUserIdAndStatus(1L, LoanStatus.PENDING))
                .thenReturn(true);

        // Act
        boolean result = loanApplicationService.existsByUserIdAndStatus(1L, "PENDING");

        // Assert
        assertTrue(result);
        verify(loanApplicationRepository, times(1))
                .existsByUserIdAndStatus(1L, LoanStatus.PENDING);
    }

    @Test
    void testExistsByUserIdAndStatus_False() {
        // Arrange
        when(loanApplicationRepository.existsByUserIdAndStatus(1L, LoanStatus.APPROVED))
                .thenReturn(false);

        // Act
        boolean result = loanApplicationService.existsByUserIdAndStatus(1L, "APPROVED");

        // Assert
        assertFalse(result);
        verify(loanApplicationRepository, times(1))
                .existsByUserIdAndStatus(1L, LoanStatus.APPROVED);
    }

    @Test
    void testExistsByUserIdAndStatus_InvalidStatus() {
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> loanApplicationService.existsByUserIdAndStatus(1L, "INVALID_STATUS"));

        assertEquals("Invalid loan status: INVALID_STATUS", exception.getMessage());
        verify(loanApplicationRepository, never()).existsByUserIdAndStatus(anyLong(), any());
    }

    // ==================== COUNT BY STATUS TESTS ====================

    @Test
    void testCountByStatus_Success() {
        // Arrange
        when(loanApplicationRepository.countByStatus(LoanStatus.PENDING)).thenReturn(5L);

        // Act
        long count = loanApplicationService.countByStatus("PENDING");

        // Assert
        assertEquals(5L, count);
        verify(loanApplicationRepository, times(1)).countByStatus(LoanStatus.PENDING);
    }

    @Test
    void testCountByStatus_Zero() {
        // Arrange
        when(loanApplicationRepository.countByStatus(LoanStatus.APPROVED)).thenReturn(0L);

        // Act
        long count = loanApplicationService.countByStatus("APPROVED");

        // Assert
        assertEquals(0L, count);
        verify(loanApplicationRepository, times(1)).countByStatus(LoanStatus.APPROVED);
    }

    @Test
    void testCountByStatus_InvalidStatus() {
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> loanApplicationService.countByStatus("INVALID_STATUS"));

        assertEquals("Invalid loan status: INVALID_STATUS", exception.getMessage());
        verify(loanApplicationRepository, never()).countByStatus(any());
    }
}
