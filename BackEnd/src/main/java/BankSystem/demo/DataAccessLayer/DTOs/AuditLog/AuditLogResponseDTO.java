package BankSystem.demo.DataAccessLayer.DTOs.AuditLog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogResponseDTO {

    private Long id;
    private Long userId;
    private String userName;
    private String action;
    private String details;
    private String ipAddress;
    private LocalDateTime createdAt;
}
