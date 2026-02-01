package BankSystem.demo.BusinessLogic.Services;

import BankSystem.demo.DataAccessLayer.DTOs.SupportTicket.SupportTicketRequestDTO;
import BankSystem.demo.DataAccessLayer.DTOs.SupportTicket.SupportTicketResponseDTO;
import BankSystem.demo.DataAccessLayer.DTOs.SupportTicket.SupportTicketUpdateDTO;

import java.util.List;

public interface SupportTicketService {

    SupportTicketResponseDTO createTicket(SupportTicketRequestDTO requestDTO);

    SupportTicketResponseDTO getTicketById(Long ticketId);

    List<SupportTicketResponseDTO> getAllTickets();

    List<SupportTicketResponseDTO> getTicketsByUserId(Long userId);

    List<SupportTicketResponseDTO> getTicketsByStatus(String status);

    List<SupportTicketResponseDTO> getTicketsByCategory(String category);

    SupportTicketResponseDTO updateTicket(Long ticketId, SupportTicketUpdateDTO updateDTO);

    SupportTicketResponseDTO updateTicketStatus(Long ticketId, String status);

    void deleteTicket(Long ticketId);
}
