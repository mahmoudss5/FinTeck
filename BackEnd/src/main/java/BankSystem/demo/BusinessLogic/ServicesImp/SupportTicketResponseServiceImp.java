package BankSystem.demo.BusinessLogic.ServicesImp;

import BankSystem.demo.Aspect.Auditing.AuditLog;
import BankSystem.demo.Aspect.Security.OnlyForSameUser;
import BankSystem.demo.Aspect.Security.RequiresAdmin;
import BankSystem.demo.BusinessLogic.Services.SupportTicketResponseService;
import BankSystem.demo.Config.CurrentUserProvider;
import BankSystem.demo.DataAccessLayer.DTOs.SupportTicketResponse.SupportTicketResponseDTO;
import BankSystem.demo.DataAccessLayer.DTOs.SupportTicketResponse.SupportTicketResponseRequestDTO;
import BankSystem.demo.DataAccessLayer.Entites.SupportTicket;
import BankSystem.demo.DataAccessLayer.Entites.SupportTicketResponse;
import BankSystem.demo.DataAccessLayer.Entites.User;
import BankSystem.demo.DataAccessLayer.Repositories.SupportTicketRepository;
import BankSystem.demo.DataAccessLayer.Repositories.SupportTicketResponseRepository;
import BankSystem.demo.DataAccessLayer.Repositories.UserRepositorie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupportTicketResponseServiceImp implements SupportTicketResponseService {

    private final SupportTicketResponseRepository responseRepository;
    private final SupportTicketRepository ticketRepository;
    private final UserRepositorie userRepository;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @Transactional
    @AuditLog
    public SupportTicketResponseDTO createResponse(SupportTicketResponseRequestDTO requestDTO) {
        SupportTicket ticket = ticketRepository.findById(requestDTO.getTicketId())
                .orElseThrow(() -> new RuntimeException("Support ticket not found with id: " + requestDTO.getTicketId()));

        Long currentUserId = currentUserProvider.getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("User not authenticated");
        }
        User sender = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + currentUserId));

        SupportTicketResponse response = SupportTicketResponse.builder()
                .ticket(ticket)
                .sender(sender)
                .responseMessage(requestDTO.getResponseMessage())
                .build();

        SupportTicketResponse savedResponse = responseRepository.save(response);
        return convertToDTO(savedResponse);
    }

    @Override
    public SupportTicketResponseDTO getResponseById(Long responseId) {
        SupportTicketResponse response = responseRepository.findById(responseId)
                .orElseThrow(() -> new RuntimeException("Response not found with id: " + responseId));
        return convertToDTO(response);
    }

    @Override
    public List<SupportTicketResponseDTO> getResponsesByTicketId(Long ticketId) {
        if (!ticketRepository.existsById(ticketId)) {
            throw new RuntimeException("Support ticket not found with id: " + ticketId);
        }
        return responseRepository.findByTicketIdOrderByCreatedAtAsc(ticketId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @OnlyForSameUser
    public List<SupportTicketResponseDTO> getResponsesBySenderId(Long senderId) {
        if (!userRepository.existsById(senderId)) {
            throw new RuntimeException("User not found with id: " + senderId);
        }
        return responseRepository.findBySenderId(senderId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @RequiresAdmin
    @AuditLog
    public void deleteResponse(Long responseId) {
        if (!responseRepository.existsById(responseId)) {
            throw new RuntimeException("Response not found with id: " + responseId);
        }
        responseRepository.deleteById(responseId);
    }

    // Helper method to convert Entity to DTO
    private SupportTicketResponseDTO convertToDTO(SupportTicketResponse response) {
        return SupportTicketResponseDTO.builder()
                .id(response.getId())
                .ticketId(response.getTicket() != null ? response.getTicket().getId() : null)
                .senderId(response.getSender() != null ? response.getSender().getId() : null)
                .senderUserName(response.getSender() != null ? response.getSender().getUserName() : null)
                .responseMessage(response.getResponseMessage())
                .createdAt(response.getCreatedAt())
                .build();
    }
}
