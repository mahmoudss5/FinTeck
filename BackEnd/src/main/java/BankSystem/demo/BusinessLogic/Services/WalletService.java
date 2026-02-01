package BankSystem.demo.BusinessLogic.Services;

import BankSystem.demo.DataAccessLayer.DTOs.Transaction.TransactionRequestDTO;
import BankSystem.demo.DataAccessLayer.DTOs.Transaction.TransactionResponseDTO;
import BankSystem.demo.DataAccessLayer.DTOs.Wallet.WalletRequestDTO;
import BankSystem.demo.DataAccessLayer.DTOs.Wallet.WalletResponseDTO;
import BankSystem.demo.DataAccessLayer.Entites.WalletCurrency;

public interface WalletService {
    WalletResponseDTO createWallet(WalletRequestDTO walletRequestDTO);

    TransactionResponseDTO TransferFunds(TransactionRequestDTO transactionRequestDTO);

    WalletResponseDTO getWalletById(Long id);

    java.util.List<WalletResponseDTO> getAllWallets();

    WalletResponseDTO deactivateWallet(Long id);

    WalletResponseDTO deleteWallet(Long id);

    Boolean existsByUserIdAndWalletCurrency(Long userId, WalletCurrency walletCurrency);
}
