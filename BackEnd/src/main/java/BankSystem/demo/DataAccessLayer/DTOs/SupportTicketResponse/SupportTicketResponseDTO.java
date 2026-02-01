package BankSystem.demo.DataAccessLayer.DTOs.SupportTicketResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportTicketResponseDTO {

    private Long id;
    private Long ticketId;
    private Long senderId;
    private String senderUserName;
    private String responseMessage;
    private LocalDateTime createdAt;
}
