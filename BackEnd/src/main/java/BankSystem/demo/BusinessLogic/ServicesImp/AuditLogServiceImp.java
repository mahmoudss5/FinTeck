package BankSystem.demo.BusinessLogic.ServicesImp;

import BankSystem.demo.Aspect.Preformance.PerformanceAspect;
import BankSystem.demo.Aspect.Security.RequiresAdmin;
import BankSystem.demo.BusinessLogic.Services.AuditLogService;
import BankSystem.demo.DataAccessLayer.Entites.AuditLog;
import BankSystem.demo.DataAccessLayer.Entites.User;
import BankSystem.demo.DataAccessLayer.Repositories.AuditLogRepositorie;
import BankSystem.demo.DataAccessLayer.Repositories.UserRepositorie;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuditLogServiceImp implements AuditLogService {

    private final AuditLogRepositorie auditLogRepositorie;
    private final UserRepositorie userRepositorie;

    @Override
    @Async
    public void logAudit(String action, String details, String ipAddress, Long userId) {
        User user = null;
        if (userId != null) {
            user = userRepositorie.findById(userId)
                    .orElse(null);
        }

        AuditLog auditLog = AuditLog.builder()
                .action(action)
                .details(details)
                .user(user)
                .ipAddress(ipAddress)
                .createdAt(java.time.LocalDateTime.now())
                .build();
        auditLogRepositorie.save(auditLog);
    }

    @Override
    @RequiresAdmin
    @PerformanceAspect
    public List<AuditLog> findAllByUserId(Long userId) {
        return auditLogRepositorie.findAll();
    }
}
