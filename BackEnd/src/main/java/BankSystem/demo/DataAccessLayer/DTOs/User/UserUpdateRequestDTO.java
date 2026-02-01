package BankSystem.demo.DataAccessLayer.DTOs.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequestDTO {



    private String oldPassword;

    @Size(min = 8, message = "New password must be at least 8 characters")
    private String newPassword;


}
