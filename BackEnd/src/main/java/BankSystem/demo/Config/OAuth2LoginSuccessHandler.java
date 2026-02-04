package BankSystem.demo.Config;

import BankSystem.demo.BusinessLogic.Services.UserService;
import BankSystem.demo.DataAccessLayer.Entites.RoleType;
import BankSystem.demo.DataAccessLayer.Entites.SecurityUser;
import BankSystem.demo.DataAccessLayer.Entites.User;
import BankSystem.demo.DataAccessLayer.Entites.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserService userService;
    private final JwtService jwtService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = token.getPrincipal();


        Map<String, Object> attributes = oAuth2User.getAttributes();

        String username = (String) attributes.get("login");
        String email = (String) attributes.get("email");
        String fullName = (String) attributes.get("name");

        if (email == null) {
            email = username + "@github.com";
        }

        if (fullName == null) {
            fullName = username;
        }

        String[] names = fullName.split(" ");
        String firstName = names[0];
        String lastName = names.length > 1 ? names[names.length - 1] : "";

        String finalEmail = email;
        String finalFirstName = firstName;
        String finalLastName = lastName;
        String finalUsername = username;

        User user = userService.findByEmail(finalEmail).orElseGet(() -> {
            User newUser = new User();
            newUser.setUserName(finalUsername);
            newUser.setEmail(finalEmail);
            newUser.setFirstName(finalFirstName);
            newUser.setLastName(finalLastName);
            newUser.setPassword(""); // Set empty password for OAuth users
            userService.save(newUser);
            return newUser;
        });


        String jwtToken = jwtService.generateToken(new SecurityUser(user));

        String targetUrl = frontendUrl + "/oauth2/redirect?token=" + jwtToken;
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}