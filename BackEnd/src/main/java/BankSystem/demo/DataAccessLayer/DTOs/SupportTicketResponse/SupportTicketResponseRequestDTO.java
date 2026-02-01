package BankSystem.demo.DataAccessLayer.DTOs.SupportTicketResponse;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportTicketResponseRequestDTO {

    @NotNull(message = "Ticket ID is required")
    private Long ticketId;

    @NotBlank(message = "Response message is required")
    private String responseMessage;
}
