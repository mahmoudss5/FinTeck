package BankSystem.demo.DataAccessLayer.DTOs.SupportTicket;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportTicketUpdateDTO {

    @Size(max = 255, message = "Subject must not exceed 255 characters")
    private String subject;

    private String description;

    private String ticketStatus;

    private String ticketCategory;
}
