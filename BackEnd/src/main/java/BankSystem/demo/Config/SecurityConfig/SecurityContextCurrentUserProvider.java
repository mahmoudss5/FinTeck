package BankSystem.demo.Config.SecurityConfig;

import BankSystem.demo.Config.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityContextCurrentUserProvider implements CurrentUserProvider {

    @Override
    public Long getCurrentUserId() {
        return SecurityUtils.getCurrentUserId();
    }
}
