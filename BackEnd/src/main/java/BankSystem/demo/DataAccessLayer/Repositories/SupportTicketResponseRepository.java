package BankSystem.demo.DataAccessLayer.Repositories;

import BankSystem.demo.DataAccessLayer.Entites.SupportTicketResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportTicketResponseRepository extends JpaRepository<SupportTicketResponse, Long> {

    List<SupportTicketResponse> findByTicketId(Long ticketId);

    List<SupportTicketResponse> findBySenderId(Long senderId);

    List<SupportTicketResponse> findByTicketIdOrderByCreatedAtAsc(Long ticketId);
}
