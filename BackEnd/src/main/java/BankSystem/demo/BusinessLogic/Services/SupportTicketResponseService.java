package BankSystem.demo.BusinessLogic.Services;

import BankSystem.demo.DataAccessLayer.DTOs.SupportTicketResponse.SupportTicketResponseDTO;
import BankSystem.demo.DataAccessLayer.DTOs.SupportTicketResponse.SupportTicketResponseRequestDTO;

import java.util.List;

public interface SupportTicketResponseService {

    SupportTicketResponseDTO createResponse(SupportTicketResponseRequestDTO requestDTO);

    SupportTicketResponseDTO getResponseById(Long responseId);

    List<SupportTicketResponseDTO> getResponsesByTicketId(Long ticketId);

    List<SupportTicketResponseDTO> getResponsesBySenderId(Long senderId);

    void deleteResponse(Long responseId);
}
