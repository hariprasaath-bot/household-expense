package in.house.financial.dto;

import in.house.financial.model.BankAccount;
import in.house.financial.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Data Transfer Object for user profile information.
 * Used for communication between frontend and backend.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private String id;
    private String name;
    private String email;
    private Date memberSince;
    private List<BankAccount> bankAccounts;
    private List<Category> categories;
}