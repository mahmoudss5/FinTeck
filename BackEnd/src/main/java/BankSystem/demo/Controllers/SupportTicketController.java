package BankSystem.demo.Controllers;

import BankSystem.demo.BusinessLogic.Services.SupportTicketService;
import BankSystem.demo.DataAccessLayer.DTOs.SupportTicket.SupportTicketRequestDTO;
import BankSystem.demo.DataAccessLayer.DTOs.SupportTicket.SupportTicketResponseDTO;
import BankSystem.demo.DataAccessLayer.DTOs.SupportTicket.SupportTicketUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Support Ticket Controller", description = "APIs for managing support tickets")
@RestController
@RequestMapping("/support-tickets/api")
@RequiredArgsConstructor
public class SupportTicketController {

    private final SupportTicketService supportTicketService;

    @Operation(summary = "Create Support Ticket", description = "Create a new support ticket")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<SupportTicketResponseDTO> createTicket(
            @RequestBody @Valid SupportTicketRequestDTO requestDTO) {
        SupportTicketResponseDTO response = supportTicketService.createTicket(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get Ticket by ID", description = "Retrieve a support ticket by its ID")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{ticketId}")
    public ResponseEntity<SupportTicketResponseDTO> getTicketById(@PathVariable Long ticketId) {
        SupportTicketResponseDTO response = supportTicketService.getTicketById(ticketId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get All Tickets", description = "Retrieve all support tickets")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<List<SupportTicketResponseDTO>> getAllTickets() {
        List<SupportTicketResponseDTO> response = supportTicketService.getAllTickets();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get Tickets by User ID", description = "Retrieve all support tickets for a specific user")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SupportTicketResponseDTO>> getTicketsByUserId(@PathVariable Long userId) {
        List<SupportTicketResponseDTO> response = supportTicketService.getTicketsByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get Tickets by Status", description = "Retrieve all support tickets with a specific status")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/status/{status}")
    public ResponseEntity<List<SupportTicketResponseDTO>> getTicketsByStatus(@PathVariable String status) {
        List<SupportTicketResponseDTO> response = supportTicketService.getTicketsByStatus(status);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get Tickets by Category", description = "Retrieve all support tickets with a specific category")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/category/{category}")
    public ResponseEntity<List<SupportTicketResponseDTO>> getTicketsByCategory(@PathVariable String category) {
        List<SupportTicketResponseDTO> response = supportTicketService.getTicketsByCategory(category);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Update Ticket Status", description = "Update only the status of a support ticket")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{ticketId}/status")
    public ResponseEntity<SupportTicketResponseDTO> updateTicketStatus(
            @PathVariable Long ticketId,
            @RequestParam String status) {
        SupportTicketResponseDTO response = supportTicketService.updateTicketStatus(ticketId, status);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete Ticket", description = "Delete a support ticket")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{ticketId}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long ticketId) {
        supportTicketService.deleteTicket(ticketId);
        return ResponseEntity.noContent().build();
    }
}
