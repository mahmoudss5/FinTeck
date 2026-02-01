package BankSystem.demo.DataAccessLayer.Repositories;

import BankSystem.demo.DataAccessLayer.Entites.TicketCategory;
import BankSystem.demo.DataAccessLayer.Entites.TicketStatus;
import BankSystem.demo.DataAccessLayer.Entites.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SupportTicketRepositoryTest {

    @Autowired
    private UserRepositorie userRepositorie;
    @Autowired
    private SupportTicketRepository supportTicketRepository;


    private User testUser;
    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .userName("test_user")
                .email("test@gmail.com")
                .firstName("Test")
                .lastName("User")
                .password("hashed_password")
                .build();
        testUser = userRepositorie.save(testUser);
    }

    @Test
    void itShouldFindNoOpenTicketsForNewUser() {
        // when
        Boolean hasOpenTickets = supportTicketRepository.existsByUserIdAndStatus(testUser.getId(), TicketStatus.OPEN);
        System.out.println(hasOpenTickets);
        // then
        assertFalse(hasOpenTickets);
    }

    @Test
    void  itShouldFindOpenTicketsForNewUser() {
        // given
        var ticket = BankSystem.demo.DataAccessLayer.Entites.SupportTicket.builder()
                .user(testUser)
                .subject("Test Subject")
                .description("Test Description")
                .status(TicketStatus.OPEN)
                .category(TicketCategory.TECHNICAL_SUPPORT)
                .build();
        supportTicketRepository.save(ticket);
        // when
        Boolean hasOpenTickets = supportTicketRepository.existsByUserIdAndStatus(testUser.getId(), TicketStatus.OPEN);
        System.out.println(hasOpenTickets);
        // then
        assertTrue(hasOpenTickets);
    }
    @Test
    void itShouldReturnAllUserTickets() {
        // given
        var ticket1 = BankSystem.demo.DataAccessLayer.Entites.SupportTicket.builder()
                .user(testUser)
                .subject("Test Subject 1")
                .description("Test Description 1")
                .status(TicketStatus.OPEN)
                .category(TicketCategory.TECHNICAL_SUPPORT)
                .build();
        var ticket2 = BankSystem.demo.DataAccessLayer.Entites.SupportTicket.builder()
                .user(testUser)
                .subject("Test Subject 2")
                .description("Test Description 2")
                .status(TicketStatus.CLOSED)
                .category(TicketCategory.TECHNICAL_SUPPORT)
                .build();
        supportTicketRepository.save(ticket1);
        supportTicketRepository.save(ticket2);
        // when
        var tickets = supportTicketRepository.findByUserId(testUser.getId());
        // then
        assertEquals(2, tickets.size());
    }

    @Test
    void itShouldFindTicketsByStatus() {
        // given
        var openTicket = BankSystem.demo.DataAccessLayer.Entites.SupportTicket.builder()
                .user(testUser)
                .subject("Open Ticket")
                .description("Description")
                .status(TicketStatus.OPEN)
                .category(TicketCategory.TECHNICAL_SUPPORT)
                .build();
        var closedTicket = BankSystem.demo.DataAccessLayer.Entites.SupportTicket.builder()
                .user(testUser)
                .subject("Closed Ticket")
                .description("Description")
                .status(TicketStatus.CLOSED)
                .category(TicketCategory.BILLING_INQUIRY)
                .build();
        supportTicketRepository.save(openTicket);
        supportTicketRepository.save(closedTicket);

        // when
        var openTickets = supportTicketRepository.findByStatus(TicketStatus.OPEN);

        // then
        assertEquals(1, openTickets.size());
        assertEquals("Open Ticket", openTickets.get(0).getSubject());
    }

    @Test
    void itShouldFindTicketsByCategory() {
        // given
        var techTicket = BankSystem.demo.DataAccessLayer.Entites.SupportTicket.builder()
                .user(testUser)
                .subject("Tech Ticket")
                .description("Description")
                .status(TicketStatus.OPEN)
                .category(TicketCategory.TECHNICAL_SUPPORT)
                .build();
        var billingTicket = BankSystem.demo.DataAccessLayer.Entites.SupportTicket.builder()
                .user(testUser)
                .subject("Billing Ticket")
                .description("Description")
                .status(TicketStatus.OPEN)
                .category(TicketCategory.BILLING_INQUIRY)
                .build();
        supportTicketRepository.save(techTicket);
        supportTicketRepository.save(billingTicket);

        // when
        var techTickets = supportTicketRepository.findByCategory(TicketCategory.TECHNICAL_SUPPORT);

        // then
        assertEquals(1, techTickets.size());
        assertEquals("Tech Ticket", techTickets.get(0).getSubject());
    }

    @Test
    void itShouldFindTicketsByUserIdAndStatus() {
        // given
        User anotherUser = User.builder()
                .userName("another_user")
                .email("another@gmail.com")
                .firstName("Another")
                .lastName("User")
                .password("hashed_password")
                .build();
        anotherUser = userRepositorie.save(anotherUser);

        var userOpenTicket = BankSystem.demo.DataAccessLayer.Entites.SupportTicket.builder()
                .user(testUser)
                .subject("User Open Ticket")
                .status(TicketStatus.OPEN)
                .build();
        var userClosedTicket = BankSystem.demo.DataAccessLayer.Entites.SupportTicket.builder()
                .user(testUser)
                .subject("User Closed Ticket")
                .status(TicketStatus.CLOSED)
                .build();
        var anotherUserOpenTicket = BankSystem.demo.DataAccessLayer.Entites.SupportTicket.builder()
                .user(anotherUser)
                .subject("Another User Open Ticket")
                .status(TicketStatus.OPEN)
                .build();
        supportTicketRepository.save(userOpenTicket);
        supportTicketRepository.save(userClosedTicket);
        supportTicketRepository.save(anotherUserOpenTicket);

        // when
        var tickets = supportTicketRepository.findByUserIdAndStatus(testUser.getId(), TicketStatus.OPEN);

        // then
        assertEquals(1, tickets.size());
        assertEquals("User Open Ticket", tickets.get(0).getSubject());
    }

    @Test
    void itShouldReturnEmptyListWhenNoTicketsFound() {
        // when
        var tickets = supportTicketRepository.findByUserId(testUser.getId());

        // then
        assertTrue(tickets.isEmpty());
    }

    @Test
    void itShouldSaveAndRetrieveTicketWithAllFields() {
        // given
        var ticket = BankSystem.demo.DataAccessLayer.Entites.SupportTicket.builder()
                .user(testUser)
                .subject("Complete Ticket")
                .description("Full description here")
                .status(TicketStatus.IN_PROGRESS)
                .category(TicketCategory.ACCOUNT_ISSUE)
                .build();

        // when
        var savedTicket = supportTicketRepository.save(ticket);
        var foundTicket = supportTicketRepository.findById(savedTicket.getId()).orElse(null);

        // then
        assertNotNull(foundTicket);
        assertEquals("Complete Ticket", foundTicket.getSubject());
        assertEquals("Full description here", foundTicket.getDescription());
        assertEquals(TicketStatus.IN_PROGRESS, foundTicket.getStatus());
        assertEquals(TicketCategory.ACCOUNT_ISSUE, foundTicket.getCategory());
        assertNotNull(foundTicket.getCreatedAt());
    }

    @Test
    void itShouldUpdateTicketStatus() {
        // given
        var ticket = BankSystem.demo.DataAccessLayer.Entites.SupportTicket.builder()
                .user(testUser)
                .subject("Status Update Test")
                .status(TicketStatus.OPEN)
                .build();
        var savedTicket = supportTicketRepository.save(ticket);

        // when
        savedTicket.setStatus(TicketStatus.RESOLVED);
        var updatedTicket = supportTicketRepository.save(savedTicket);

        // then
        assertEquals(TicketStatus.RESOLVED, updatedTicket.getStatus());
    }

    @Test
    void itShouldDeleteTicket() {
        // given
        var ticket = BankSystem.demo.DataAccessLayer.Entites.SupportTicket.builder()
                .user(testUser)
                .subject("Delete Test")
                .status(TicketStatus.OPEN)
                .build();
        var savedTicket = supportTicketRepository.save(ticket);
        Long ticketId = savedTicket.getId();

        // when
        supportTicketRepository.deleteById(ticketId);

        // then
        assertTrue(supportTicketRepository.findById(ticketId).isEmpty());
    }
}