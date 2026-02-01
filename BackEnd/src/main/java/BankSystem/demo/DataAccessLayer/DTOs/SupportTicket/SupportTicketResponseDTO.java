package BankSystem.demo.DataAccessLayer.DTOs.SupportTicket;

import BankSystem.demo.DataAccessLayer.Entites.TicketCategory;
import BankSystem.demo.DataAccessLayer.Entites.TicketStatus;
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
    private Long userId;
    private String userName;
    private String subject;
    private String description;
    private TicketStatus ticketStatus;
    private TicketCategory ticketCategory;
    private LocalDateTime createdAt;
}
