package BankSystem.demo.Config.SecurityConfig;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
@Component
@RequiredArgsConstructor
@EnableWebSecurity
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;


    // lets us assume that the security context is like a box that holds the authentication information of the currently logged-in user.


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
         // get Authorization header from request
      final   String authHeader = request.getHeader("Authorization");
       final String jwt;
       final  String userEmail;

       // check if header is valid and if not continue filter chain
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // extract jwt token and user email
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);

        // validate token and set authentication in security context
        // if userEmail is not null and no authentication is set
        if (userEmail != null && org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication() == null) {

            // load user details from database
            var userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // if token is valid, set authentication
            if (jwtService.isTokenValid(jwt, userDetails)) {
                var authToken = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(new org.springframework.security.web.authentication.WebAuthenticationDetailsSource().buildDetails(request));

                 // set authentication in security context
                org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
