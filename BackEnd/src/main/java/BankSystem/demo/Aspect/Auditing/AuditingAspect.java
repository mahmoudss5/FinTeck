package BankSystem.demo.Aspect.Auditing;

import BankSystem.demo.Config.ClientIpResolver;
import BankSystem.demo.BusinessLogic.Services.AuditLogService;
import BankSystem.demo.Config.SecurityUtils;
import BankSystem.demo.DataAccessLayer.Entites.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static BankSystem.demo.Config.SecurityUtils.getCurrentUserId;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AuditingAspect {

    private final AuditLogService auditLogService;
    private final ClientIpResolver clientIpResolver;

    @AfterReturning("@annotation(BankSystem.demo.Aspect.Auditing.AuditLog)")
    public void logAuditLog(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String details = "Executed method: " + methodName;

        String ipAddress = clientIpResolver.getClientIpAddress();

        Long userId = getCurrentUserId();
        if (userId == null) {
            throw new IllegalStateException("Unable to retrieve current user ID for auditing.");
        }

        auditLogService.logAudit("AUDIT_LOG", details, ipAddress, userId);
    }

}
