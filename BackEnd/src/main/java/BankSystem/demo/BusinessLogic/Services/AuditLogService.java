package BankSystem.demo.BusinessLogic.Services;


import BankSystem.demo.DataAccessLayer.Entites.AuditLog;

import java.util.List;

public interface AuditLogService {
    void logAudit(String action, String details, String ipAddress, Long userId);
    List<AuditLog> findAllByUserId(Long userId);

}
