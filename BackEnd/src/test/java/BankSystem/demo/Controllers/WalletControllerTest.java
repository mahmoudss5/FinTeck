package BankSystem.demo.Controllers;

import BankSystem.demo.BusinessLogic.Services.WalletService;
import BankSystem.demo.DataAccessLayer.DTOs.Transaction.TransactionRequestDTO;
import BankSystem.demo.DataAccessLayer.DTOs.Transaction.TransactionResponseDTO;
import BankSystem.demo.DataAccessLayer.DTOs.Wallet.WalletRequestDTO;
import BankSystem.demo.DataAccessLayer.DTOs.Wallet.WalletResponseDTO;
import BankSystem.demo.DataAccessLayer.Entites.WalletCurrency;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WalletController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private WalletService walletService;

    private WalletRequestDTO walletRequestDTO;
    private WalletResponseDTO walletResponseDTO;

    @BeforeEach
    void setUp() {
        walletRequestDTO = WalletRequestDTO.builder()
                .currency(WalletCurrency.USD)
                .initialBalance(BigDecimal.valueOf(1000.00))
                .build();

        walletResponseDTO = WalletResponseDTO.builder()
                .id(1L)
                .userId(1L)
                .userName("testuser")
                .currency(WalletCurrency.USD)
                .balance(BigDecimal.valueOf(1000.00))
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    // ==================== CREATE WALLET TESTS ====================

    @Test
    void testCreateWallet_Success() throws Exception {
        given(walletService.createWallet(any(WalletRequestDTO.class)))
                .willReturn(walletResponseDTO);

        mockMvc.perform(post("/wallets/api/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(walletRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.userName").value("testuser"))
                .andExpect(jsonPath("$.balance").value(1000.00));

        verify(walletService, times(1)).createWallet(any(WalletRequestDTO.class));
    }

    @Test
    void testCreateWallet_UserNotFound() throws Exception {
        given(walletService.createWallet(any(WalletRequestDTO.class)))
                .willThrow(new RuntimeException("User not found"));

        mockMvc.perform(post("/wallets/api/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(walletRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("User not found"));

        verify(walletService, times(1)).createWallet(any(WalletRequestDTO.class));
    }

    @Test
    void testCreateWallet_WalletAlreadyExists() throws Exception {
        given(walletService.createWallet(any(WalletRequestDTO.class)))
                .willThrow(new RuntimeException("Wallet with this currency already exists for the user"));

        mockMvc.perform(post("/wallets/api/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(walletRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Wallet with this currency already exists for the user"));
    }

    // ==================== GET WALLET BY ID TESTS ====================

    @Test
    void testGetWalletById_Success() throws Exception {
        given(walletService.getWalletById(1L)).willReturn(walletResponseDTO);

        mockMvc.perform(get("/wallets/api/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.userName").value("testuser"));

        verify(walletService, times(1)).getWalletById(1L);
    }

    @Test
    void testGetWalletById_NotFound() throws Exception {
        given(walletService.getWalletById(999L))
                .willThrow(new RuntimeException("Wallet not found"));

        mockMvc.perform(get("/wallets/api/999"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Wallet not found"));

        verify(walletService, times(1)).getWalletById(999L);
    }

    // ==================== GET ALL WALLETS TESTS ====================

    @Test
    void testGetAllWallets_Success() throws Exception {
        WalletResponseDTO wallet2 = WalletResponseDTO.builder()
                .id(2L)
                .userId(2L)
                .userName("anotheruser")
                .currency(WalletCurrency.EUR)
                .balance(BigDecimal.valueOf(2000.00))
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        List<WalletResponseDTO> wallets = Arrays.asList(walletResponseDTO, wallet2);
        given(walletService.getAllWallets()).willReturn(wallets);

        mockMvc.perform(get("/wallets/api/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));

        verify(walletService, times(1)).getAllWallets();
    }

    @Test
    void testGetAllWallets_EmptyList() throws Exception {
        given(walletService.getAllWallets()).willReturn(Arrays.asList());

        mockMvc.perform(get("/wallets/api/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(walletService, times(1)).getAllWallets();
    }

    // ==================== DEACTIVATE WALLET TESTS ====================

    @Test
    void testDeactivateWallet_Success() throws Exception {
        WalletResponseDTO deactivatedWallet = WalletResponseDTO.builder()
                .id(1L)
                .userId(1L)
                .userName("testuser")
                .currency(WalletCurrency.USD)
                .balance(BigDecimal.valueOf(1000.00))
                .active(false)
                .createdAt(LocalDateTime.now())
                .build();

        given(walletService.deactivateWallet(1L)).willReturn(deactivatedWallet);

        mockMvc.perform(put("/wallets/api/deactivate/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.active").value(false));

        verify(walletService, times(1)).deactivateWallet(1L);
    }

    @Test
    void testDeactivateWallet_NotFound() throws Exception {
        given(walletService.deactivateWallet(999L))
                .willThrow(new RuntimeException("Wallet not found"));

        mockMvc.perform(put("/wallets/api/deactivate/999"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Wallet not found"));

        verify(walletService, times(1)).deactivateWallet(999L);
    }

    @Test
    void testDeactivateWallet_AlreadyDeactivated() throws Exception {
        given(walletService.deactivateWallet(1L))
                .willThrow(new RuntimeException("Wallet is already deactivated"));

        mockMvc.perform(put("/wallets/api/deactivate/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Wallet is already deactivated"));

        verify(walletService, times(1)).deactivateWallet(1L);
    }

    // ==================== DELETE WALLET TESTS ====================

    @Test
    void testDeleteWallet_Success() throws Exception {
        doNothing().when(walletService).deleteWallet(1L);

        mockMvc.perform(delete("/wallets/api/delete/1"))
                .andExpect(status().isNoContent());

        verify(walletService, times(1)).deleteWallet(1L);
    }

    @Test
    void testDeleteWallet_NotFound() throws Exception {
        doThrow(new RuntimeException("Wallet not found"))
                .when(walletService).deleteWallet(999L);

        mockMvc.perform(delete("/wallets/api/delete/999"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Wallet not found"));

        verify(walletService, times(1)).deleteWallet(999L);
    }

    // ==================== TRANSFER FUNDS TESTS ====================

    @Test
    void testTransferFunds_Success() throws Exception {
        TransactionRequestDTO transferRequest = TransactionRequestDTO.builder()
                .senderWalletId(1L)
                .receiverUserName("receiver")
                .currency(WalletCurrency.USD)
                .amount(BigDecimal.valueOf(100.00))
                .build();

        TransactionResponseDTO transferResponse = TransactionResponseDTO.builder()
                .senderWalletId(1L)
                .receiverUserName("receiver")
                .currency(WalletCurrency.USD)
                .amount(BigDecimal.valueOf(100.00))
                .build();

        given(walletService.TransferFunds(any(TransactionRequestDTO.class)))
                .willReturn(transferResponse);

        mockMvc.perform(put("/wallets/api/transfer")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.senderWalletId").value(1L))
                .andExpect(jsonPath("$.receiverUserName").value("receiver"))
                .andExpect(jsonPath("$.amount").value(100.00))
                .andExpect(jsonPath("$.currency").value("USD"));

        verify(walletService, times(1)).TransferFunds(any(TransactionRequestDTO.class));
    }

    @Test
    void testTransferFunds_SourceWalletNotFound() throws Exception {
        TransactionRequestDTO transferRequest = TransactionRequestDTO.builder()
                .senderWalletId(999L)
                .receiverUserName("receiver")
                .currency(WalletCurrency.USD)
                .amount(BigDecimal.valueOf(100.00))
                .build();

        given(walletService.TransferFunds(any(TransactionRequestDTO.class)))
                .willThrow(new RuntimeException("Source wallet not found"));

        mockMvc.perform(put("/wallets/api/transfer")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Source wallet not found"));
    }

    @Test
    void testTransferFunds_InsufficientFunds() throws Exception {
        TransactionRequestDTO transferRequest = TransactionRequestDTO.builder()
                .senderWalletId(1L)
                .receiverUserName("receiver")
                .currency(WalletCurrency.USD)
                .amount(BigDecimal.valueOf(10000.00))
                .build();

        given(walletService.TransferFunds(any(TransactionRequestDTO.class)))
                .willThrow(new RuntimeException("Insufficient funds in the source wallet"));

        mockMvc.perform(put("/wallets/api/transfer")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Insufficient funds in the source wallet"));
    }

    @Test
    void testTransferFunds_InactiveWallet() throws Exception {
        TransactionRequestDTO transferRequest = TransactionRequestDTO.builder()
                .senderWalletId(1L)
                .receiverUserName("receiver")
                .currency(WalletCurrency.USD)
                .amount(BigDecimal.valueOf(100.00))
                .build();

        given(walletService.TransferFunds(any(TransactionRequestDTO.class)))
                .willThrow(new RuntimeException("One or both wallets are inactive"));

        mockMvc.perform(put("/wallets/api/transfer")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("One or both wallets are inactive"));
    }
}
