package BankSystem.demo.Controllers;

import BankSystem.demo.BusinessLogic.Services.LoanApplicationService;
import BankSystem.demo.DataAccessLayer.DTOs.LoanApplication.LoanApplicationRequestDTO;
import BankSystem.demo.DataAccessLayer.DTOs.LoanApplication.LoanApplicationResponseDTO;
import BankSystem.demo.DataAccessLayer.DTOs.LoanApplication.LoanApplicationUpdateDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanApplicationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class LoanApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LoanApplicationService loanApplicationService;

    private LoanApplicationRequestDTO requestDTO;
    private LoanApplicationResponseDTO responseDTO;
    private LoanApplicationUpdateDTO updateDTO;

    @BeforeEach
    void setUp() {
        requestDTO = LoanApplicationRequestDTO.builder()
                .fullName("John Doe")
                .email("john.doe@example.com")
                .phoneNumber("+1234567890")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .maritalStatus("Single")
                .employmentStatus("Employed")
                .monthlyIncome(new BigDecimal("5000.00"))
                .employerName("Tech Company")
                .yearsAtCurrentJob("3")
                .loanPurpose("Personal")
                .requestedAmount(new BigDecimal("10000.00"))
                .build();

        responseDTO = LoanApplicationResponseDTO.builder()
                .id(1L)
                .userId(1L)
                .fullName("John Doe")
                .email("john.doe@example.com")
                .phoneNumber("+1234567890")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .maritalStatus("Single")
                .employmentStatus("Employed")
                .monthlyIncome(new BigDecimal("5000.00"))
                .employerName("Tech Company")
                .yearsAtCurrentJob("3")
                .loanPurpose("Personal")
                .requestedAmount(new BigDecimal("10000.00"))
                .status("PENDING")
                .appliedAt(LocalDateTime.now())
                .build();

        updateDTO = LoanApplicationUpdateDTO.builder()
                .id(1L)
                .status("APPROVED")
                .build();
    }

    // ==================== CREATE LOAN APPLICATION TESTS ====================

    @Test
    void testCreateLoanApplication_Success() throws Exception {
        // Arrange
        given(loanApplicationService.createLoanApplication(any(LoanApplicationRequestDTO.class)))
                .willReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/loan-applications/api")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.fullName").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.requestedAmount").value(10000.00));

        verify(loanApplicationService, times(1)).createLoanApplication(any(LoanApplicationRequestDTO.class));
    }

    @Test
    void testCreateLoanApplication_InvalidData() throws Exception {
        // Arrange - Create invalid DTO (missing required fields)
        LoanApplicationRequestDTO invalidDTO = LoanApplicationRequestDTO.builder().build();

        // Act & Assert
        mockMvc.perform(post("/loan-applications/api")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());

        verify(loanApplicationService, never()).createLoanApplication(any(LoanApplicationRequestDTO.class));
    }

    @Test
    void testCreateLoanApplication_UserNotFound() throws Exception {
        // Arrange
        given(loanApplicationService.createLoanApplication(any(LoanApplicationRequestDTO.class)))
                .willThrow(new RuntimeException("User not found with ID: 1"));

        // Act & Assert
        mockMvc.perform(post("/loan-applications/api")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("User not found with ID: 1"));

        verify(loanApplicationService, times(1)).createLoanApplication(any(LoanApplicationRequestDTO.class));
    }

    // ==================== GET LOAN APPLICATION BY ID TESTS ====================

    @Test
    void testGetLoanApplicationById_Success() throws Exception {
        // Arrange
        given(loanApplicationService.getLoanApplicationById(1L)).willReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(get("/loan-applications/api/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.fullName").value("John Doe"))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(loanApplicationService, times(1)).getLoanApplicationById(1L);
    }

    @Test
    void testGetLoanApplicationById_NotFound() throws Exception {
        // Arrange
        given(loanApplicationService.getLoanApplicationById(999L))
                .willThrow(new RuntimeException("Loan application not found with ID: 999"));

        // Act & Assert
        mockMvc.perform(get("/loan-applications/api/999"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Loan application not found with ID: 999"));

        verify(loanApplicationService, times(1)).getLoanApplicationById(999L);
    }

    // ==================== GET ALL LOAN APPLICATIONS TESTS ====================

    @Test
    void testGetAllLoanApplications_Success() throws Exception {
        // Arrange
        LoanApplicationResponseDTO response2 = LoanApplicationResponseDTO.builder()
                .id(2L)
                .userId(2L)
                .fullName("Jane Smith")
                .email("jane.smith@example.com")
                .phoneNumber("+9876543210")
                .dateOfBirth(LocalDate.of(1985, 5, 15))
                .maritalStatus("Married")
                .employmentStatus("Self-Employed")
                .monthlyIncome(new BigDecimal("6000.00"))
                .loanPurpose("Business")
                .requestedAmount(new BigDecimal("20000.00"))
                .status("APPROVED")
                .appliedAt(LocalDateTime.now())
                .build();

        List<LoanApplicationResponseDTO> applications = Arrays.asList(responseDTO, response2);
        given(loanApplicationService.getAllLoanApplications()).willReturn(applications);

        // Act & Assert
        mockMvc.perform(get("/loan-applications/api"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[0].fullName").value("John Doe"))
                .andExpect(jsonPath("$[1].fullName").value("Jane Smith"));

        verify(loanApplicationService, times(1)).getAllLoanApplications();
    }

    @Test
    void testGetAllLoanApplications_EmptyList() throws Exception {
        // Arrange
        given(loanApplicationService.getAllLoanApplications()).willReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/loan-applications/api"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(loanApplicationService, times(1)).getAllLoanApplications();
    }

    // ==================== GET LOAN APPLICATIONS BY USER ID TESTS ====================

    @Test
    void testGetLoanApplicationsByUserId_Success() throws Exception {
        // Arrange
        given(loanApplicationService.getLoanApplicationsByUserId(1L))
                .willReturn(Arrays.asList(responseDTO));

        // Act & Assert
        mockMvc.perform(get("/loan-applications/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].fullName").value("John Doe"));

        verify(loanApplicationService, times(1)).getLoanApplicationsByUserId(1L);
    }

    @Test
    void testGetLoanApplicationsByUserId_EmptyList() throws Exception {
        // Arrange
        given(loanApplicationService.getLoanApplicationsByUserId(999L))
                .willReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/loan-applications/api/user/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(loanApplicationService, times(1)).getLoanApplicationsByUserId(999L);
    }

    // ==================== GET LOAN APPLICATIONS BY STATUS TESTS ====================

    @Test
    void testGetLoanApplicationsByStatus_Success() throws Exception {
        // Arrange
        given(loanApplicationService.getLoanApplicationsByStatus("PENDING"))
                .willReturn(Arrays.asList(responseDTO));

        // Act & Assert
        mockMvc.perform(get("/loan-applications/api/status/PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("PENDING"));

        verify(loanApplicationService, times(1)).getLoanApplicationsByStatus("PENDING");
    }

    @Test
    void testGetLoanApplicationsByStatus_InvalidStatus() throws Exception {
        // Arrange
        given(loanApplicationService.getLoanApplicationsByStatus("INVALID_STATUS"))
                .willThrow(new RuntimeException("Invalid loan status: INVALID_STATUS"));

        // Act & Assert
        mockMvc.perform(get("/loan-applications/api/status/INVALID_STATUS"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid loan status: INVALID_STATUS"));

        verify(loanApplicationService, times(1)).getLoanApplicationsByStatus("INVALID_STATUS");
    }

    // ==================== UPDATE LOAN APPLICATION STATUS TESTS ====================

    @Test
    void testUpdateLoanApplicationStatus_Success() throws Exception {
        // Arrange
        LoanApplicationResponseDTO updatedResponse = LoanApplicationResponseDTO.builder()
                .id(1L)
                .userId(1L)
                .fullName("John Doe")
                .email("john.doe@example.com")
                .phoneNumber("+1234567890")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .maritalStatus("Single")
                .employmentStatus("Employed")
                .monthlyIncome(new BigDecimal("5000.00"))
                .loanPurpose("Personal")
                .requestedAmount(new BigDecimal("10000.00"))
                .status("APPROVED")
                .appliedAt(LocalDateTime.now())
                .build();

        given(loanApplicationService.updateLoanApplicationStatus(any(LoanApplicationUpdateDTO.class)))
                .willReturn(updatedResponse);

        // Act & Assert
        mockMvc.perform(patch("/loan-applications/api/status")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("APPROVED"));

        verify(loanApplicationService, times(1)).updateLoanApplicationStatus(any(LoanApplicationUpdateDTO.class));
    }

    @Test
    void testUpdateLoanApplicationStatus_NotFound() throws Exception {
        // Arrange
        given(loanApplicationService.updateLoanApplicationStatus(any(LoanApplicationUpdateDTO.class)))
                .willThrow(new RuntimeException("Loan application not found with ID: 999"));

        // Act & Assert
        mockMvc.perform(patch("/loan-applications/api/status")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Loan application not found with ID: 999"));

        verify(loanApplicationService, times(1)).updateLoanApplicationStatus(any(LoanApplicationUpdateDTO.class));
    }

    // ==================== DELETE LOAN APPLICATION TESTS ====================

    @Test
    void testDeleteLoanApplication_Success() throws Exception {
        // Arrange
        doNothing().when(loanApplicationService).deleteLoanApplication(1L);

        // Act & Assert
        mockMvc.perform(delete("/loan-applications/api/1"))
                .andExpect(status().isNoContent());

        verify(loanApplicationService, times(1)).deleteLoanApplication(1L);
    }

    @Test
    void testDeleteLoanApplication_NotFound() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Loan application not found with ID: 999"))
                .when(loanApplicationService).deleteLoanApplication(999L);

        // Act & Assert
        mockMvc.perform(delete("/loan-applications/api/999"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Loan application not found with ID: 999"));

        verify(loanApplicationService, times(1)).deleteLoanApplication(999L);
    }

    // ==================== EXISTS BY USER ID AND STATUS TESTS ====================

    @Test
    void testExistsByUserIdAndStatus_True() throws Exception {
        // Arrange
        given(loanApplicationService.existsByUserIdAndStatus(1L, "PENDING"))
                .willReturn(true);

        // Act & Assert
        mockMvc.perform(get("/loan-applications/api/exists/1/PENDING"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(loanApplicationService, times(1)).existsByUserIdAndStatus(1L, "PENDING");
    }

    @Test
    void testExistsByUserIdAndStatus_False() throws Exception {
        // Arrange
        given(loanApplicationService.existsByUserIdAndStatus(1L, "APPROVED"))
                .willReturn(false);

        // Act & Assert
        mockMvc.perform(get("/loan-applications/api/exists/1/APPROVED"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        verify(loanApplicationService, times(1)).existsByUserIdAndStatus(1L, "APPROVED");
    }

    // ==================== COUNT BY STATUS TESTS ====================

    @Test
    void testCountByStatus_Success() throws Exception {
        // Arrange
        given(loanApplicationService.countByStatus("PENDING")).willReturn(10L);

        // Act & Assert
        mockMvc.perform(get("/loan-applications/api/count/PENDING"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));

        verify(loanApplicationService, times(1)).countByStatus("PENDING");
    }

    @Test
    void testCountByStatus_Zero() throws Exception {
        // Arrange
        given(loanApplicationService.countByStatus("REJECTED")).willReturn(0L);

        // Act & Assert
        mockMvc.perform(get("/loan-applications/api/count/REJECTED"))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));

        verify(loanApplicationService, times(1)).countByStatus("REJECTED");
    }

    @Test
    void testCountByStatus_InvalidStatus() throws Exception {
        // Arrange
        given(loanApplicationService.countByStatus("INVALID_STATUS"))
                .willThrow(new RuntimeException("Invalid loan status: INVALID_STATUS"));

        // Act & Assert
        mockMvc.perform(get("/loan-applications/api/count/INVALID_STATUS"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid loan status: INVALID_STATUS"));

        verify(loanApplicationService, times(1)).countByStatus("INVALID_STATUS");
    }
}
