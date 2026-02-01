package BankSystem.demo.DataAccessLayer.Repositories;

import BankSystem.demo.DataAccessLayer.Entites.SupportTicket;
import BankSystem.demo.DataAccessLayer.Entites.TicketCategory;
import BankSystem.demo.DataAccessLayer.Entites.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    
    List<SupportTicket> findByUserId(Long userId);
    
    List<SupportTicket> findByStatus(TicketStatus status);
    
    List<SupportTicket> findByCategory(TicketCategory category);
    
    List<SupportTicket> findByUserIdAndStatus(Long userId, TicketStatus status);

    Boolean existsByUserIdAndStatus(Long id, TicketStatus status);
}
