package BankSystem.demo.DataAccessLayer.Repositories;

import BankSystem.demo.DataAccessLayer.Entites.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SupportTicketResponseRepositoryTest {

    @Autowired
    private SupportTicketResponseRepository responseRepository;

    @Autowired
    private SupportTicketRepository ticketRepository;

    @Autowired
    private UserRepositorie userRepository;

    private User testUser;
    private User supportAgent;
    private SupportTicket testTicket;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .userName("test_user")
                .email("test@gmail.com")
                .firstName("Test")
                .lastName("User")
                .password("hashed_password")
                .build();
        testUser = userRepository.save(testUser);

        supportAgent = User.builder()
                .userName("support_agent")
                .email("support@gmail.com")
                .firstName("Support")
                .lastName("Agent")
                .password("hashed_password")
                .build();
        supportAgent = userRepository.save(supportAgent);

        testTicket = SupportTicket.builder()
                .user(testUser)
                .subject("Test Ticket")
                .description("Test Description")
                .status(TicketStatus.OPEN)
                .category(TicketCategory.TECHNICAL_SUPPORT)
                .build();
        testTicket = ticketRepository.save(testTicket);
    }

    @AfterEach
    void tearDown() {
        responseRepository.deleteAll();
        ticketRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void itShouldSaveResponse() {
        // given
        SupportTicketResponse response = SupportTicketResponse.builder()
                .ticket(testTicket)
                .sender(supportAgent)
                .responseMessage("This is a test response")
                .build();

        // when
        SupportTicketResponse savedResponse = responseRepository.save(response);

        // then
        assertNotNull(savedResponse);
        assertNotNull(savedResponse.getId());
        assertEquals("This is a test response", savedResponse.getResponseMessage());
    }

    @Test
    void itShouldFindResponsesByTicketId() {
        // given
        SupportTicketResponse response1 = SupportTicketResponse.builder()
                .ticket(testTicket)
                .sender(supportAgent)
                .responseMessage("First response")
                .build();
        SupportTicketResponse response2 = SupportTicketResponse.builder()
                .ticket(testTicket)
                .sender(testUser)
                .responseMessage("Second response")
                .build();
        responseRepository.save(response1);
        responseRepository.save(response2);

        // when
        List<SupportTicketResponse> responses = responseRepository.findByTicketId(testTicket.getId());

        // then
        assertEquals(2, responses.size());
    }

    @Test
    void itShouldFindResponsesBySenderId() {
        // given
        SupportTicketResponse response1 = SupportTicketResponse.builder()
                .ticket(testTicket)
                .sender(supportAgent)
                .responseMessage("Agent response 1")
                .build();
        SupportTicketResponse response2 = SupportTicketResponse.builder()
                .ticket(testTicket)
                .sender(supportAgent)
                .responseMessage("Agent response 2")
                .build();
        SupportTicketResponse response3 = SupportTicketResponse.builder()
                .ticket(testTicket)
                .sender(testUser)
                .responseMessage("User response")
                .build();
        responseRepository.saveAll(List.of(response1, response2, response3));

        // when
        List<SupportTicketResponse> agentResponses = responseRepository.findBySenderId(supportAgent.getId());

        // then
        assertEquals(2, agentResponses.size());
    }

    @Test
    void itShouldFindResponsesByTicketIdOrderedByCreatedAt() {
        // given
        SupportTicketResponse response1 = SupportTicketResponse.builder()
                .ticket(testTicket)
                .sender(supportAgent)
                .responseMessage("First response")
                .build();
        responseRepository.save(response1);

        SupportTicketResponse response2 = SupportTicketResponse.builder()
                .ticket(testTicket)
                .sender(testUser)
                .responseMessage("Second response")
                .build();
        responseRepository.save(response2);

        // when
        List<SupportTicketResponse> responses = responseRepository.findByTicketIdOrderByCreatedAtAsc(testTicket.getId());

        // then
        assertEquals(2, responses.size());
        assertEquals("First response", responses.get(0).getResponseMessage());
        assertEquals("Second response", responses.get(1).getResponseMessage());
    }

    @Test
    void itShouldReturnEmptyListWhenNoResponsesFound() {
        // when
        List<SupportTicketResponse> responses = responseRepository.findByTicketId(testTicket.getId());

        // then
        assertTrue(responses.isEmpty());
    }

    @Test
    void itShouldDeleteResponse() {
        // given
        SupportTicketResponse response = SupportTicketResponse.builder()
                .ticket(testTicket)
                .sender(supportAgent)
                .responseMessage("To be deleted")
                .build();
        SupportTicketResponse savedResponse = responseRepository.save(response);
        Long responseId = savedResponse.getId();

        // when
        responseRepository.deleteById(responseId);

        // then
        assertTrue(responseRepository.findById(responseId).isEmpty());
    }

    @Test
    void itShouldSetCreatedAtAutomatically() {
        // given
        SupportTicketResponse response = SupportTicketResponse.builder()
                .ticket(testTicket)
                .sender(supportAgent)
                .responseMessage("Test message")
                .build();

        // when
        SupportTicketResponse savedResponse = responseRepository.save(response);

        // then
        assertNotNull(savedResponse.getCreatedAt());
    }

    @Test
    void itShouldFindResponsesForDifferentTickets() {
        // given
        SupportTicket anotherTicket = SupportTicket.builder()
                .user(testUser)
                .subject("Another Ticket")
                .status(TicketStatus.OPEN)
                .build();
        anotherTicket = ticketRepository.save(anotherTicket);

        SupportTicketResponse response1 = SupportTicketResponse.builder()
                .ticket(testTicket)
                .sender(supportAgent)
                .responseMessage("Response to first ticket")
                .build();
        SupportTicketResponse response2 = SupportTicketResponse.builder()
                .ticket(anotherTicket)
                .sender(supportAgent)
                .responseMessage("Response to second ticket")
                .build();
        responseRepository.saveAll(List.of(response1, response2));

        // when
        List<SupportTicketResponse> firstTicketResponses = responseRepository.findByTicketId(testTicket.getId());
        List<SupportTicketResponse> secondTicketResponses = responseRepository.findByTicketId(anotherTicket.getId());

        // then
        assertEquals(1, firstTicketResponses.size());
        assertEquals(1, secondTicketResponses.size());
        assertEquals("Response to first ticket", firstTicketResponses.get(0).getResponseMessage());
        assertEquals("Response to second ticket", secondTicketResponses.get(0).getResponseMessage());
    }
}
