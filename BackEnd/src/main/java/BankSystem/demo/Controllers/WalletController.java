package BankSystem.demo.Controllers;

import BankSystem.demo.BusinessLogic.Services.WalletService;
import BankSystem.demo.DataAccessLayer.DTOs.Wallet.WalletRequestDTO;
import BankSystem.demo.DataAccessLayer.DTOs.Wallet.WalletResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import BankSystem.demo.DataAccessLayer.DTOs.Transaction.TransactionRequestDTO;
import BankSystem.demo.DataAccessLayer.DTOs.Transaction.TransactionResponseDTO;
import java.util.List;

@Tag(name = "Wallet Controller", description = "APIs for managing wallets")
@AllArgsConstructor
@RestController
@RequestMapping("/wallets/api")
public class WalletController {

  private final WalletService walletService;

  @Operation(summary = "Create Wallet", description = "Create a new wallet for a user")
  @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
  @PostMapping("/create")
  public ResponseEntity<WalletResponseDTO> createWallet(@RequestBody @Valid WalletRequestDTO walletRequestDTO) {
    WalletResponseDTO response = walletService.createWallet(walletRequestDTO);
    return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "Get Wallet by ID", description = "Retrieve wallet details by wallet ID")
  @ResponseStatus(org.springframework.http.HttpStatus.OK)
  @GetMapping("/{walletId}")
  public ResponseEntity<WalletResponseDTO> getWalletById(@PathVariable Long walletId) {
    WalletResponseDTO response = walletService.getWalletById(walletId);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Get All Wallets", description = "Retrieve all wallets")
  @ResponseStatus(org.springframework.http.HttpStatus.OK)
  @GetMapping("/all")
  public ResponseEntity<List<WalletResponseDTO>> getAllWallets() {
    List<WalletResponseDTO> response = walletService.getAllWallets();
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "deactivate Wallet", description = "deactivate a wallet by ID")
  @ResponseStatus(org.springframework.http.HttpStatus.OK)
  @PutMapping("/deactivate/{walletId}")
  public ResponseEntity<WalletResponseDTO> deactivateWallet(@PathVariable Long walletId) {
    WalletResponseDTO response = walletService.deactivateWallet(walletId);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Delete Wallet", description = "Delete a wallet by ID")
  @ResponseStatus(org.springframework.http.HttpStatus.OK)
  @DeleteMapping("/delete/{walletId}")
  public ResponseEntity<WalletResponseDTO> deleteWallet(@PathVariable Long walletId) {
    WalletResponseDTO response = walletService.deleteWallet(walletId);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Transfer Funds", description = "Transfer funds between two wallets")
  @ResponseStatus(org.springframework.http.HttpStatus.OK)
  @PutMapping("/transfer")
  public ResponseEntity<TransactionResponseDTO> transferFunds(
      @RequestBody @Valid TransactionRequestDTO transferRequestDTO) {
    TransactionResponseDTO response = walletService.TransferFunds(transferRequestDTO);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Get all wallets for current user", description = "Retrieve all wallets for the authenticated user")
  @ResponseStatus(org.springframework.http.HttpStatus.OK)
  @GetMapping("/my-wallets")
  public ResponseEntity<List<WalletResponseDTO>> getWalletsForCurrentUser() {
      List<WalletResponseDTO> response = walletService.getAllWalletsForCurrentUser();
      return ResponseEntity.ok(response);
  }

  @Operation(summary = "Get all wallets by User ID", description = "Retrieve all wallets for a specific user by their ID")
  @ResponseStatus(org.springframework.http.HttpStatus.OK)
  @GetMapping("/user/{userId}/wallets")
  public ResponseEntity<List<WalletResponseDTO>> getAllWalletsByUserId(@PathVariable Long userId) {
      List<WalletResponseDTO> response = walletService.getAllWalletsByUserId(userId);
      return ResponseEntity.ok(response);
  }
}
