package BankSystem.demo.BusinessLogic.Services;

import java.math.BigDecimal;

public interface NotificationService {
    void sendTransferSuccessEmail(String email, BigDecimal amount);
}
