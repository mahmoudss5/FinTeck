package BankSystem.demo.DataAccessLayer.DTOs.AuditLog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogRequestDTO {

    @NotBlank(message = "Action is required")
    @Size(max = 255, message = "Action must not exceed 255 characters")
    private String action;

    private String details;

    @Size(max = 50, message = "IP address must not exceed 50 characters")
    private String ipAddress;
}
