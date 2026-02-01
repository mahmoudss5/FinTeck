package BankSystem.demo.BusinessLogic.ServicesImp;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupportTicketServiceImpTest {

    @Mock
    private SupportTicketRepository supportTicketRepository;

    @Mock
    private UserRepositorie userRepository;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private SupportTicketServiceImp supportTicketService;

    private User testUser;
    private SupportTicket testTicket;
    private SupportTicketRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .userName("testuser")
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .build();

        testTicket = SupportTicket.builder()
                .id(1L)
                .user(testUser)
                .subject("Test Subject")
                .description("Test Description")
                .status(TicketStatus.OPEN)
                .category(TicketCategory.GENERAL_QUESTION)
                .createdAt(LocalDateTime.now())
                .build();

        requestDTO = SupportTicketRequestDTO.builder()
                .subject("Test Subject")
                .description("Test Description")
                .ticketCategory("GENERAL_QUESTION")
                .build();
    }

    // ==================== CREATE TICKET TESTS ====================

    @Test
    void testCreateTicket_Success() {
        // Arrange
        when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(supportTicketRepository.save(any(SupportTicket.class))).thenReturn(testTicket);

        // Act
        SupportTicketResponseDTO result = supportTicketService.createTicket(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Subject", result.getSubject());
        assertEquals(TicketStatus.OPEN, result.getTicketStatus());
        verify(userRepository, times(1)).findById(1L);
        verify(supportTicketRepository, times(1)).save(any(SupportTicket.class));
    }

    @Test
    void testCreateTicket_UserNotFound() {
        // Arrange
        when(currentUserProvider.getCurrentUserId()).thenReturn(999L);
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> supportTicketService.createTicket(requestDTO));

        assertEquals("User not found with id: 999", exception.getMessage());
        verify(supportTicketRepository, never()).save(any(SupportTicket.class));
    }

    // ==================== GET TICKET BY ID TESTS ====================

    @Test
    void testGetTicketById_Success() {
        // Arrange
        when(supportTicketRepository.findById(1L)).thenReturn(Optional.of(testTicket));

        // Act
        SupportTicketResponseDTO result = supportTicketService.getTicketById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Subject", result.getSubject());
        verify(supportTicketRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTicketById_NotFound() {
        // Arrange
        when(supportTicketRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> supportTicketService.getTicketById(999L));

        assertEquals("Support ticket not found with id: 999", exception.getMessage());
    }

    // ==================== GET ALL TICKETS TESTS ====================

    @Test
    void testGetAllTickets_Success() {
        // Arrange
        SupportTicket ticket2 = SupportTicket.builder()
                .id(2L)
                .user(testUser)
                .subject("Another Subject")
                .description("Another Description")
                .status(TicketStatus.IN_PROGRESS)
                .category(TicketCategory.TECHNICAL_SUPPORT)
                .createdAt(LocalDateTime.now())
                .build();

        when(supportTicketRepository.findAll()).thenReturn(Arrays.asList(testTicket, ticket2));

        // Act
        List<SupportTicketResponseDTO> result = supportTicketService.getAllTickets();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(supportTicketRepository, times(1)).findAll();
    }

    @Test
    void testGetAllTickets_EmptyList() {
        // Arrange
        when(supportTicketRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<SupportTicketResponseDTO> result = supportTicketService.getAllTickets();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ==================== GET TICKETS BY USER ID TESTS ====================

    @Test
    void testGetTicketsByUserId_Success() {
        // Arrange
        when(userRepository.existsById(1L)).thenReturn(true);
        when(supportTicketRepository.findByUserId(1L)).thenReturn(Arrays.asList(testTicket));

        // Act
        List<SupportTicketResponseDTO> result = supportTicketService.getTicketsByUserId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUserId());
        verify(userRepository, times(1)).existsById(1L);
        verify(supportTicketRepository, times(1)).findByUserId(1L);
    }

    @Test
    void testGetTicketsByUserId_UserNotFound() {
        // Arrange
        when(userRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> supportTicketService.getTicketsByUserId(999L));

        assertEquals("User not found with id: 999", exception.getMessage());
        verify(supportTicketRepository, never()).findByUserId(anyLong());
    }

    // ==================== GET TICKETS BY STATUS TESTS ====================

    @Test
    void testGetTicketsByStatus_Success() {
        // Arrange
        when(supportTicketRepository.findByStatus(TicketStatus.OPEN))
                .thenReturn(Arrays.asList(testTicket));

        // Act
        List<SupportTicketResponseDTO> result = supportTicketService.getTicketsByStatus("OPEN");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TicketStatus.OPEN, result.get(0).getTicketStatus());
    }

    @Test
    void testGetTicketsByStatus_InvalidStatus() {
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> supportTicketService.getTicketsByStatus("INVALID_STATUS"));

        assertTrue(exception.getMessage().contains("Invalid ticket status"));
    }

    // ==================== GET TICKETS BY CATEGORY TESTS ====================

    @Test
    void testGetTicketsByCategory_Success() {
        // Arrange
        when(supportTicketRepository.findByCategory(TicketCategory.GENERAL_QUESTION))
                .thenReturn(Arrays.asList(testTicket));

        // Act
        List<SupportTicketResponseDTO> result = supportTicketService.getTicketsByCategory("GENERAL_QUESTION");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TicketCategory.GENERAL_QUESTION, result.get(0).getTicketCategory());
    }

    @Test
    void testGetTicketsByCategory_InvalidCategory() {
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> supportTicketService.getTicketsByCategory("INVALID_CATEGORY"));

        assertTrue(exception.getMessage().contains("Invalid ticket category"));
    }

    // ==================== UPDATE TICKET TESTS ====================

    @Test
    void testUpdateTicket_Success() {
        // Arrange
        SupportTicketUpdateDTO updateDTO = SupportTicketUpdateDTO.builder()
                .subject("Updated Subject")
                .description("Updated Description")
                .ticketStatus("IN_PROGRESS")
                .build();

        when(supportTicketRepository.findById(1L)).thenReturn(Optional.of(testTicket));
        when(supportTicketRepository.save(any(SupportTicket.class))).thenAnswer(invocation -> {
            SupportTicket saved = invocation.getArgument(0);
            saved.setSubject("Updated Subject");
            saved.setStatus(TicketStatus.IN_PROGRESS);
            return saved;
        });

        // Act
        SupportTicketResponseDTO result = supportTicketService.updateTicket(1L, updateDTO);

        // Assert
        assertNotNull(result);
        verify(supportTicketRepository, times(1)).findById(1L);
        verify(supportTicketRepository, times(1)).save(any(SupportTicket.class));
    }

    @Test
    void testUpdateTicket_NotFound() {
        // Arrange
        SupportTicketUpdateDTO updateDTO = SupportTicketUpdateDTO.builder()
                .subject("Updated Subject")
                .build();

        when(supportTicketRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> supportTicketService.updateTicket(999L, updateDTO));

        assertEquals("Support ticket not found with id: 999", exception.getMessage());
        verify(supportTicketRepository, never()).save(any(SupportTicket.class));
    }

    // ==================== UPDATE TICKET STATUS TESTS ====================

    @Test
    void testUpdateTicketStatus_Success() {
        // Arrange
        when(supportTicketRepository.findById(1L)).thenReturn(Optional.of(testTicket));
        when(supportTicketRepository.save(any(SupportTicket.class))).thenAnswer(invocation -> {
            SupportTicket saved = invocation.getArgument(0);
            saved.setStatus(TicketStatus.RESOLVED);
            return saved;
        });

        // Act
        SupportTicketResponseDTO result = supportTicketService.updateTicketStatus(1L, "RESOLVED");

        // Assert
        assertNotNull(result);
        verify(supportTicketRepository, times(1)).findById(1L);
        verify(supportTicketRepository, times(1)).save(any(SupportTicket.class));
    }

    @Test
    void testUpdateTicketStatus_NotFound() {
        // Arrange
        when(supportTicketRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> supportTicketService.updateTicketStatus(999L, "RESOLVED"));

        assertEquals("Support ticket not found with id: 999", exception.getMessage());
    }

    @Test
    void testUpdateTicketStatus_InvalidStatus() {
        // Arrange
        when(supportTicketRepository.findById(1L)).thenReturn(Optional.of(testTicket));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> supportTicketService.updateTicketStatus(1L, "INVALID_STATUS"));

        assertTrue(exception.getMessage().contains("Invalid ticket status"));
    }

    // ==================== DELETE TICKET TESTS ====================

    @Test
    void testDeleteTicket_Success() {
        // Arrange
        when(supportTicketRepository.existsById(1L)).thenReturn(true);
        doNothing().when(supportTicketRepository).deleteById(1L);

        // Act
        supportTicketService.deleteTicket(1L);

        // Assert
        verify(supportTicketRepository, times(1)).existsById(1L);
        verify(supportTicketRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteTicket_NotFound() {
        // Arrange
        when(supportTicketRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> supportTicketService.deleteTicket(999L));

        assertEquals("Support ticket not found with id: 999", exception.getMessage());
        verify(supportTicketRepository, never()).deleteById(anyLong());
    }
}
