package in.house.financial.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for bank account operations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountRequest {
    private String bankName;
    private String accountNumber;
    private String accountType;
}