package BankSystem.demo.DataAccessLayer.DTOs.Wallet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletUpdateRequestDTO {

    private Long WalletId;
    private Boolean active;
}
