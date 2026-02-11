package BankSystem.demo.Controllers;

import BankSystem.demo.BusinessLogic.Services.TransactionService;
import BankSystem.demo.DataAccessLayer.DTOs.Transaction.TransactionResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Transaction Controller", description = "APIs for managing transactions")
@RestController
@RequestMapping("/transactions/api")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Get Wallet Transactions", description = "Retrieve all transactions for a specific wallet")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/wallet/{walletId}")
    public ResponseEntity<List<TransactionResponseDTO>> getWalletTransactions(@PathVariable Long walletId) {
        List<TransactionResponseDTO> response = transactionService.getWalletTransactions(walletId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get All Transactions", description = "Retrieve all transactions in the system")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions() {
        List<TransactionResponseDTO> response = transactionService.getAllTransactions();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get Transactions by Status", description = "Retrieve all transactions with a specific status")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByStatus(@PathVariable String status) {
        List<TransactionResponseDTO> response = transactionService.findTransactionsByStatus(status);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Generate Monthly Report", description = "Generate a monthly transaction report for a wallet")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/report/{walletId}/{month}")
    public ResponseEntity<String> generateMonthlyReport(
            @PathVariable Long walletId,
            @PathVariable int month) {
        String report = transactionService.generateMonthlyReport(walletId, month);
        return ResponseEntity.ok(report);
    }
/*
    @Operation(summary = "Get All Transactions for Current User", description = "Retrieve all transactions for the currently authenticated user")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/current-user")
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactionsForCurrentUser() {
        List<TransactionResponseDTO> response = transactionService.getAllTransactionsForCurrentUser();
        return ResponseEntity.ok(response);
    }*/

    @Operation(summary = "Get All Transactions by Wallet ID", description = "Retrieve all transactions for a specific wallet ID")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/wallet-transactions/{walletId}")
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactionsByWalletId(@PathVariable Long walletId) {
        List<TransactionResponseDTO> response = transactionService.findAllTransactionsByWalletId(walletId);
        return ResponseEntity.ok(response);
    }



    @Operation(summary = "get All Transactions for User Beta Version", description = "Retrieve all transactions for a specific user ID (Beta Version)")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user-All-transactions")
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactionsForUserBetaVersion() {
        List<TransactionResponseDTO> response = transactionService.getAllTransactionsForUserBetaVersion();
        return ResponseEntity.ok(response);
    }

}
