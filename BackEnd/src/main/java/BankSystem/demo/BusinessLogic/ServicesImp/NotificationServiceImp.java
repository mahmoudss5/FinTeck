package BankSystem.demo.BusinessLogic.ServicesImp;

import BankSystem.demo.BusinessLogic.Services.NotificationService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class NotificationServiceImp implements NotificationService {
    @Override
    public void sendTransferSuccessEmail(String email, BigDecimal amount) {
        //Todo: Implement email sending logic here
        System.out.println("Email sent to " + email + " for successful transfer of amount: " + amount);
    }
}
