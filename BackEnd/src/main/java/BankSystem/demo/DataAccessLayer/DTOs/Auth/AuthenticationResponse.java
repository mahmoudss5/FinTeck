package BankSystem.demo.DataAccessLayer.DTOs.Auth;

import BankSystem.demo.DataAccessLayer.DTOs.User.UserResponseDTO;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    private String userName;
}