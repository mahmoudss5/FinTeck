package BankSystem.demo.Aspect.Loging;

import BankSystem.demo.BusinessLogic.Services.AuditLogService;
import BankSystem.demo.Config.SecurityConfig.JwtService;
import BankSystem.demo.DataAccessLayer.DTOs.Auth.AuthenticationResponse;
import BankSystem.demo.DataAccessLayer.Entites.User;
import BankSystem.demo.DataAccessLayer.Repositories.UserRepositorie;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
public class LoggingAspect {

    private final AuditLogService auditLogService;
    private final JwtService jwtService;
    private final UserRepositorie userRepositorie;

    @AfterReturning(pointcut = "execution(* BankSystem.demo.BusinessLogic.Services.UserService.loginUser(..))", returning = "result")
    public void logLogin(JoinPoint joinPoint, Object result) {

        if (result instanceof AuthenticationResponse response) {
            String token = response.getToken();

            String username = jwtService.extractUsername(token);
            Long userId = null;
            User user = userRepositorie.findByEmail(username).orElseThrow(
                    () -> new RuntimeException("User not found with email: " + username)
            );
            userId = user.getId();
            String details = "User " + username + " logged in successfully.";

            auditLogService.logAudit("LOGIN", details, null, userId);
        }
    }
}