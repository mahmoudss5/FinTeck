package BankSystem.demo.DataAccessLayer.Repositories;

import BankSystem.demo.DataAccessLayer.Entites.LoanApplication;
import BankSystem.demo.DataAccessLayer.Entites.LoanStatus;
import BankSystem.demo.DataAccessLayer.Entites.Role;
import BankSystem.demo.DataAccessLayer.Entites.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class LoanApplicationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    private User testUser;
    private LoanApplication testLoan;

    @BeforeEach
    void setUp() {
        // Create test user
        Role role = Role.builder().name("User").build();
        entityManager.persist(role);

        testUser = User.builder()
                .userName("testuser")
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .password("hashedPassword")
                .roles(Set.of(role))
                .build();
        entityManager.persist(testUser);

        // Create test loan application
        testLoan = LoanApplication.builder()
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
                .build();
        entityManager.persist(testLoan);
        entityManager.flush();
    }

    // ==================== FIND BY USER ID TESTS ====================

    @Test
    void testFindByUserId_Success() {
        // Act
        List<LoanApplication> applications = loanApplicationRepository.findByUserId(testUser.getId());

        // Assert
        assertNotNull(applications);
        assertEquals(1, applications.size());
        assertEquals(testLoan.getId(), applications.get(0).getId());
        assertEquals(testUser.getId(), applications.get(0).getUser().getId());
    }

    @Test
    void testFindByUserId_NotFound() {
        // Act
        List<LoanApplication> applications = loanApplicationRepository.findByUserId(999L);

        // Assert
        assertNotNull(applications);
        assertTrue(applications.isEmpty());
    }

    // ==================== FIND BY STATUS TESTS ====================

    @Test
    void testFindByStatus_Success() {
        // Act
        List<LoanApplication> applications = loanApplicationRepository.findByStatus(LoanStatus.PENDING);

        // Assert
        assertNotNull(applications);
        assertEquals(1, applications.size());
        assertEquals(LoanStatus.PENDING, applications.get(0).getStatus());
    }

    @Test
    void testFindByStatus_MultipleResults() {
        // Arrange - Create another pending loan
        LoanApplication anotherLoan = LoanApplication.builder()
                .user(testUser)
                .fullName("Another User")
                .email("another@example.com")
                .phoneNumber("+9876543210")
                .dateOfBirth(LocalDate.of(1985, 5, 15))
                .maritalStatus("Married")
                .employmentStatus("Self-Employed")
                .monthlyIncome(new BigDecimal("6000.00"))
                .employerName("Self")
                .yearsAtCurrentJob("5")
                .loanPurpose("Business")
                .requestedAmount(new BigDecimal("20000.00"))
                .status(LoanStatus.PENDING)
                .build();
        entityManager.persist(anotherLoan);
        entityManager.flush();

        // Act
        List<LoanApplication> applications = loanApplicationRepository.findByStatus(LoanStatus.PENDING);

        // Assert
        assertNotNull(applications);
        assertEquals(2, applications.size());
    }

    @Test
    void testFindByStatus_EmptyResult() {
        // Act
        List<LoanApplication> applications = loanApplicationRepository.findByStatus(LoanStatus.APPROVED);

        // Assert
        assertNotNull(applications);
        assertTrue(applications.isEmpty());
    }

    // ==================== FIND BY USER ID AND STATUS TESTS ====================

    @Test
    void testFindByUserIdAndStatus_Success() {
        // Act
        List<LoanApplication> applications = loanApplicationRepository
                .findByUserIdAndStatus(testUser.getId(), LoanStatus.PENDING);

        // Assert
        assertNotNull(applications);
        assertEquals(1, applications.size());
        assertEquals(testUser.getId(), applications.get(0).getUser().getId());
        assertEquals(LoanStatus.PENDING, applications.get(0).getStatus());
    }

    @Test
    void testFindByUserIdAndStatus_NotFound() {
        // Act
        List<LoanApplication> applications = loanApplicationRepository
                .findByUserIdAndStatus(testUser.getId(), LoanStatus.APPROVED);

        // Assert
        assertNotNull(applications);
        assertTrue(applications.isEmpty());
    }

    // ==================== EXISTS BY USER ID AND STATUS TESTS ====================

    @Test
    void testExistsByUserIdAndStatus_True() {
        // Act
        boolean exists = loanApplicationRepository
                .existsByUserIdAndStatus(testUser.getId(), LoanStatus.PENDING);

        // Assert
        assertTrue(exists);
    }

    @Test
    void testExistsByUserIdAndStatus_False() {
        // Act
        boolean exists = loanApplicationRepository
                .existsByUserIdAndStatus(testUser.getId(), LoanStatus.APPROVED);

        // Assert
        assertFalse(exists);
    }

    // ==================== COUNT BY STATUS TESTS ====================

    @Test
    void testCountByStatus_Success() {
        // Act
        long count = loanApplicationRepository.countByStatus(LoanStatus.PENDING);

        // Assert
        assertEquals(1, count);
    }

    @Test
    void testCountByStatus_Zero() {
        // Act
        long count = loanApplicationRepository.countByStatus(LoanStatus.APPROVED);

        // Assert
        assertEquals(0, count);
    }

    @Test
    void testCountByStatus_MultipleApplications() {
        // Arrange - Create more pending applications
        LoanApplication loan2 = LoanApplication.builder()
                .user(testUser)
                .fullName("User Two")
                .email("user2@example.com")
                .phoneNumber("+1111111111")
                .dateOfBirth(LocalDate.of(1988, 3, 10))
                .maritalStatus("Single")
                .employmentStatus("Employed")
                .monthlyIncome(new BigDecimal("7000.00"))
                .loanPurpose("Education")
                .requestedAmount(new BigDecimal("15000.00"))
                .status(LoanStatus.PENDING)
                .build();

        LoanApplication loan3 = LoanApplication.builder()
                .user(testUser)
                .fullName("User Three")
                .email("user3@example.com")
                .phoneNumber("+2222222222")
                .dateOfBirth(LocalDate.of(1992, 7, 20))
                .maritalStatus("Married")
                .employmentStatus("Employed")
                .monthlyIncome(new BigDecimal("8000.00"))
                .loanPurpose("Home")
                .requestedAmount(new BigDecimal("50000.00"))
                .status(LoanStatus.PENDING)
                .build();

        entityManager.persist(loan2);
        entityManager.persist(loan3);
        entityManager.flush();

        // Act
        long count = loanApplicationRepository.countByStatus(LoanStatus.PENDING);

        // Assert
        assertEquals(3, count);
    }

    // ==================== FIND BY EMAIL CONTAINING TESTS ====================

    @Test
    void testFindByEmailContainingIgnoreCase_Success() {
        // Act
        List<LoanApplication> applications = loanApplicationRepository
                .findByEmailContainingIgnoreCase("test");

        // Assert
        assertNotNull(applications);
        assertEquals(1, applications.size());
        assertTrue(applications.get(0).getEmail().toLowerCase().contains("test"));
    }

    @Test
    void testFindByEmailContainingIgnoreCase_CaseInsensitive() {
        // Act
        List<LoanApplication> applications = loanApplicationRepository
                .findByEmailContainingIgnoreCase("TEST");

        // Assert
        assertNotNull(applications);
        assertEquals(1, applications.size());
    }

    @Test
    void testFindByEmailContainingIgnoreCase_NotFound() {
        // Act
        List<LoanApplication> applications = loanApplicationRepository
                .findByEmailContainingIgnoreCase("nonexistent");

        // Assert
        assertNotNull(applications);
        assertTrue(applications.isEmpty());
    }

    // ==================== CRUD TESTS ====================

    @Test
    void testSaveLoanApplication_Success() {
        // Arrange
        LoanApplication newLoan = LoanApplication.builder()
                .user(testUser)
                .fullName("New Applicant")
                .email("new@example.com")
                .phoneNumber("+3333333333")
                .dateOfBirth(LocalDate.of(1995, 12, 25))
                .maritalStatus("Single")
                .employmentStatus("Employed")
                .monthlyIncome(new BigDecimal("4500.00"))
                .loanPurpose("Car")
                .requestedAmount(new BigDecimal("25000.00"))
                .status(LoanStatus.PENDING)
                .build();

        // Act
        LoanApplication savedLoan = loanApplicationRepository.save(newLoan);

        // Assert
        assertNotNull(savedLoan);
        assertNotNull(savedLoan.getId());
        assertEquals("New Applicant", savedLoan.getFullName());
        assertEquals(LoanStatus.PENDING, savedLoan.getStatus());
    }

    @Test
    void testUpdateLoanApplication_Success() {
        // Arrange
        testLoan.setStatus(LoanStatus.APPROVED);

        // Act
        LoanApplication updatedLoan = loanApplicationRepository.save(testLoan);

        // Assert
        assertNotNull(updatedLoan);
        assertEquals(LoanStatus.APPROVED, updatedLoan.getStatus());
    }

    @Test
    void testDeleteLoanApplication_Success() {
        // Arrange
        Long loanId = testLoan.getId();

        // Act
        loanApplicationRepository.deleteById(loanId);

        // Assert
        assertFalse(loanApplicationRepository.existsById(loanId));
    }
}
