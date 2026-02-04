package BankSystem.demo.Aspect.Security;

import BankSystem.demo.BusinessLogic.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import static BankSystem.demo.Config.SecurityUtils.getCurrentUserId;
import BankSystem.demo.DataAccessLayer.Entites.User;
import BankSystem.demo.DataAccessLayer.Repositories.UserRepositorie;

@Aspect
@Component // this is a component for the class
@Slf4j // this is a logger for the class to log the execution time of the method
@RequiredArgsConstructor
public class SecurityAspect {

    private final UserService userService;


    @Before("execution(* BankSystem.demo.BusinessLogic.Services.UserService.promoteToAdmin(..)) || @annotation(requiresAdmin)")
    public void checkAdmin(JoinPoint joinPoint, RequiresAdmin requiresAdmin) {
        Long Userid = getCurrentUserId();
        if (Userid == null) {
            throw new RuntimeException("User not found");
        }
        if (!userService.isAdmin(Userid)) {
            log.error("Access denied: User {} is not an admin", Userid);
            throw new RuntimeException("User is not an admin");

        }

    }

    @Before("execution(* BankSystem.demo.BusinessLogic.Services.UserService.getUserById(..)) || @annotation(onlyForSameUser)")
    public void checkSameUser(JoinPoint joinPoint, OnlyForSameUser onlyForSameUser) {
        Long Userid = getCurrentUserId();
        Object[] args = joinPoint.getArgs();
        Long targetUserId = (Long) args[0];
        if (Userid == null) {
            throw new RuntimeException("User not found");
        }
        if (!Userid.equals(targetUserId) && !userService.isAdmin(Userid)) {
            throw new RuntimeException("Access denied: Not the same user or admin");
        }
    }
}
