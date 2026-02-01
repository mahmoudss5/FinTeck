package BankSystem.demo.DataAccessLayer.DTOs.User;

import BankSystem.demo.DataAccessLayer.DTOs.Wallet.WalletResponseDTO;
import BankSystem.demo.DataAccessLayer.Entites.Wallet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private Long id;
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;

    private List<WalletResponseDTO> wallets;

    private Set<String> roles;
}
