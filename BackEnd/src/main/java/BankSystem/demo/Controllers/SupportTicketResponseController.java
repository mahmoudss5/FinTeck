package BankSystem.demo.Controllers;

import BankSystem.demo.BusinessLogic.Services.SupportTicketResponseService;
import BankSystem.demo.DataAccessLayer.DTOs.SupportTicketResponse.SupportTicketResponseDTO;
import BankSystem.demo.DataAccessLayer.DTOs.SupportTicketResponse.SupportTicketResponseRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Support Ticket Response Controller", description = "APIs for managing support ticket responses")
@RestController
@RequestMapping("/support-ticket-responses/api")
@RequiredArgsConstructor
public class SupportTicketResponseController {

    private final SupportTicketResponseService supportTicketResponseService;

    @Operation(summary = "Create Response", description = "Create a new response for a support ticket")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<SupportTicketResponseDTO> createResponse(
            @RequestBody @Valid SupportTicketResponseRequestDTO requestDTO) {
        SupportTicketResponseDTO response = supportTicketResponseService.createResponse(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get Response by ID", description = "Retrieve a specific response by its ID")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{responseId}")
    public ResponseEntity<SupportTicketResponseDTO> getResponseById(@PathVariable Long responseId) {
        SupportTicketResponseDTO response = supportTicketResponseService.getResponseById(responseId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get Responses by Ticket ID", description = "Retrieve all responses for a specific support ticket")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<List<SupportTicketResponseDTO>> getResponsesByTicketId(@PathVariable Long ticketId) {
        List<SupportTicketResponseDTO> responses = supportTicketResponseService.getResponsesByTicketId(ticketId);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Get Responses by Sender ID", description = "Retrieve all responses from a specific sender")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/sender/{senderId}")
    public ResponseEntity<List<SupportTicketResponseDTO>> getResponsesBySenderId(@PathVariable Long senderId) {
        List<SupportTicketResponseDTO> responses = supportTicketResponseService.getResponsesBySenderId(senderId);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Delete Response", description = "Delete a support ticket response")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{responseId}")
    public ResponseEntity<Void> deleteResponse(@PathVariable Long responseId) {
        supportTicketResponseService.deleteResponse(responseId);
        return ResponseEntity.noContent().build();
    }
}
