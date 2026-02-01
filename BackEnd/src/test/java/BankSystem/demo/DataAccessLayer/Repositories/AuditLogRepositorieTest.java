package BankSystem.demo.DataAccessLayer.Repositories;

import BankSystem.demo.DataAccessLayer.Entites.AuditLog;
import BankSystem.demo.DataAccessLayer.Entites.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AuditLogRepositorieTest {

    @Autowired
    private AuditLogRepositorie auditLogRepository;

    @Autowired
    private UserRepositorie userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .userName("test_user")
                .email("test@gmail.com")
                .firstName("Test")
                .lastName("User")
                .password("hashed_password")
                .build();
        testUser = userRepository.save(testUser);
    }

    @AfterEach
    void tearDown() {
        auditLogRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void itShouldSaveAuditLog() {
        // given
        AuditLog auditLog = AuditLog.builder()
                .user(testUser)
                .action("LOGIN")
                .details("User logged in successfully")
                .ipAddress("192.168.1.1")
                .build();

        // when
        AuditLog savedLog = auditLogRepository.save(auditLog);

        // then
        assertNotNull(savedLog);
        assertNotNull(savedLog.getId());
        assertEquals("LOGIN", savedLog.getAction());
        assertEquals("User logged in successfully", savedLog.getDetails());
        assertEquals("192.168.1.1", savedLog.getIpAddress());
    }

    @Test
    void itShouldFindAuditLogById() {
        // given
        AuditLog auditLog = AuditLog.builder()
                .user(testUser)
                .action("TRANSFER")
                .details("Transferred $100")
                .ipAddress("10.0.0.1")
                .build();
        AuditLog savedLog = auditLogRepository.save(auditLog);

        // when
        AuditLog foundLog = auditLogRepository.findById(savedLog.getId()).orElse(null);

        // then
        assertNotNull(foundLog);
        assertEquals("TRANSFER", foundLog.getAction());
    }

    @Test
    void itShouldFindAllAuditLogs() {
        // given
        AuditLog log1 = AuditLog.builder()
                .user(testUser)
                .action("LOGIN")
                .details("First login")
                .build();
        AuditLog log2 = AuditLog.builder()
                .user(testUser)
                .action("TRANSFER")
                .details("Made a transfer")
                .build();
        AuditLog log3 = AuditLog.builder()
                .user(testUser)
                .action("LOGOUT")
                .details("User logged out")
                .build();
        auditLogRepository.saveAll(List.of(log1, log2, log3));

        // when
        List<AuditLog> allLogs = auditLogRepository.findAll();

        // then
        assertEquals(3, allLogs.size());
    }

    @Test
    void itShouldSetCreatedAtAutomatically() {
        // given
        AuditLog auditLog = AuditLog.builder()
                .user(testUser)
                .action("TEST_ACTION")
                .build();

        // when
        AuditLog savedLog = auditLogRepository.save(auditLog);

        // then
        assertNotNull(savedLog.getCreatedAt());
    }

    @Test
    void itShouldSaveAuditLogWithoutUser() {
        // given - system action without user
        AuditLog auditLog = AuditLog.builder()
                .action("SYSTEM_STARTUP")
                .details("Application started")
                .build();

        // when
        AuditLog savedLog = auditLogRepository.save(auditLog);

        // then
        assertNotNull(savedLog);
        assertNull(savedLog.getUser());
        assertEquals("SYSTEM_STARTUP", savedLog.getAction());
    }

    @Test
    void itShouldDeleteAuditLog() {
        // given
        AuditLog auditLog = AuditLog.builder()
                .user(testUser)
                .action("TO_DELETE")
                .build();
        AuditLog savedLog = auditLogRepository.save(auditLog);
        Long logId = savedLog.getId();

        // when
        auditLogRepository.deleteById(logId);

        // then
        assertTrue(auditLogRepository.findById(logId).isEmpty());
    }

    @Test
    void itShouldSaveAuditLogWithLongDetails() {
        // given
        String longDetails = "A".repeat(1000);
        AuditLog auditLog = AuditLog.builder()
                .user(testUser)
                .action("LONG_ACTION")
                .details(longDetails)
                .build();

        // when
        AuditLog savedLog = auditLogRepository.save(auditLog);

        // then
        assertNotNull(savedLog);
        assertEquals(1000, savedLog.getDetails().length());
    }
}
