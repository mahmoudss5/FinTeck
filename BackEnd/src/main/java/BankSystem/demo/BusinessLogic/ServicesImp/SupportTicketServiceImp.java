package BankSystem.demo.BusinessLogic.ServicesImp;

import BankSystem.demo.Aspect.Auditing.AuditLog;
import BankSystem.demo.Aspect.Preformance.PerformanceAspect;
import BankSystem.demo.Aspect.Security.OnlyForSameUser;
import BankSystem.demo.Aspect.Security.RequiresAdmin;
import BankSystem.demo.BusinessLogic.Services.SupportTicketService;
import BankSystem.demo.Config.CurrentUserProvider;
import BankSystem.demo.DataAccessLayer.DTOs.SupportTicket.SupportTicketRequestDTO;
import BankSystem.demo.DataAccessLayer.DTOs.SupportTicket.SupportTicketResponseDTO;
import BankSystem.demo.DataAccessLayer.DTOs.SupportTicket.SupportTicketUpdateDTO;
import BankSystem.demo.DataAccessLayer.Entites.SupportTicket;
import BankSystem.demo.DataAccessLayer.Entites.TicketCategory;
import BankSystem.demo.DataAccessLayer.Entites.TicketStatus;
import BankSystem.demo.DataAccessLayer.Entites.User;
import BankSystem.demo.DataAccessLayer.Repositories.SupportTicketRepository;
import BankSystem.demo.DataAccessLayer.Repositories.UserRepositorie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupportTicketServiceImp implements SupportTicketService {

    private final SupportTicketRepository supportTicketRepository;
    private final UserRepositorie userRepository;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @Transactional
    @PerformanceAspect
    @AuditLog
    @OnlyForSameUser
    public SupportTicketResponseDTO createTicket(SupportTicketRequestDTO requestDTO) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("User not authenticated");
        }
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + currentUserId));

        SupportTicket ticket = SupportTicket.builder()
                .user(user)
                .subject(requestDTO.getSubject())
                .description(requestDTO.getDescription())
                .status(TicketStatus.OPEN)
                .category(parseCategory(requestDTO.getTicketCategory()))
                .build();

        SupportTicket savedTicket = supportTicketRepository.save(ticket);
        return convertToDTO(savedTicket);
    }

    @Override
    @OnlyForSameUser
    public SupportTicketResponseDTO getTicketById(Long ticketId) {
        SupportTicket ticket = supportTicketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Support ticket not found with id: " + ticketId));
        return convertToDTO(ticket);
    }

    @Override
    @PerformanceAspect
    @RequiresAdmin
    public List<SupportTicketResponseDTO> getAllTickets() {
        return supportTicketRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @OnlyForSameUser
    public List<SupportTicketResponseDTO> getTicketsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        return supportTicketRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @RequiresAdmin
    public List<SupportTicketResponseDTO> getTicketsByStatus(String status) {
        TicketStatus ticketStatus = parseStatus(status);
        return supportTicketRepository.findByStatus(ticketStatus).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @RequiresAdmin
    public List<SupportTicketResponseDTO> getTicketsByCategory(String category) {
        TicketCategory ticketCategory = parseCategory(category);
        return supportTicketRepository.findByCategory(ticketCategory).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @AuditLog
    @OnlyForSameUser
    public SupportTicketResponseDTO updateTicket(Long ticketId, SupportTicketUpdateDTO updateDTO) {
        SupportTicket ticket = supportTicketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Support ticket not found with id: " + ticketId));

        if (updateDTO.getSubject() != null && !updateDTO.getSubject().isBlank()) {
            ticket.setSubject(updateDTO.getSubject());
        }
        if (updateDTO.getDescription() != null) {
            ticket.setDescription(updateDTO.getDescription());
        }
        if (updateDTO.getTicketStatus() != null) {
            ticket.setStatus(parseStatus(updateDTO.getTicketStatus()));
        }
        if (updateDTO.getTicketCategory() != null) {
            ticket.setCategory(parseCategory(updateDTO.getTicketCategory()));
        }

        SupportTicket updatedTicket = supportTicketRepository.save(ticket);
        return convertToDTO(updatedTicket);
    }

    @Override
    @Transactional
    @RequiresAdmin
    @AuditLog
    public SupportTicketResponseDTO updateTicketStatus(Long ticketId, String status) {
        SupportTicket ticket = supportTicketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Support ticket not found with id: " + ticketId));

        ticket.setStatus(parseStatus(status));
        SupportTicket updatedTicket = supportTicketRepository.save(ticket);
        return convertToDTO(updatedTicket);
    }

    @Override
    @Transactional
    @RequiresAdmin
    @AuditLog
    public void deleteTicket(Long ticketId) {
        if (!supportTicketRepository.existsById(ticketId)) {
            throw new RuntimeException("Support ticket not found with id: " + ticketId);
        }
        supportTicketRepository.deleteById(ticketId);
    }

    // Helper method to convert Entity to DTO
    private SupportTicketResponseDTO convertToDTO(SupportTicket ticket) {
        return SupportTicketResponseDTO.builder()
                .id(ticket.getId())
                .userId(ticket.getUser() != null ? ticket.getUser().getId() : null)
                .userName(ticket.getUser() != null ? ticket.getUser().getUserName() : null)
                .subject(ticket.getSubject())
                .description(ticket.getDescription())
                .ticketStatus(ticket.getStatus())
                .ticketCategory(ticket.getCategory())
                .createdAt(ticket.getCreatedAt())
                .build();
    }

    // Helper method to parse status string to enum
    private TicketStatus parseStatus(String status) {
        if (status == null) {
            return TicketStatus.OPEN;
        }
        try {
            return TicketStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid ticket status: " + status +
                    ". Valid values are: OPEN, IN_PROGRESS, RESOLVED, CLOSED");
        }
    }

    // Helper method to parse category string to enum
    private TicketCategory parseCategory(String category) {
        if (category == null || category.isBlank()) {
            return null;
        }
        try {
            return TicketCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid ticket category: " + category +
                    ". Valid values are: ACCOUNT_ISSUE, TRANSACTION_PROBLEM, TECHNICAL_SUPPORT, " +
                    "BILLING_INQUIRY, SECURITY_CONCERN, GENERAL_QUESTION, FEATURE_REQUEST, OTHER");
        }
    }
}
