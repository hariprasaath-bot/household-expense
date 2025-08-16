package in.house.financial.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.house.financial.model.BankAccount;
import in.house.financial.model.Category;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
@Document(collection = "inmates")
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    private String id;
    
    private String name;
    
    @NonNull
    private String email;
    
    private String status;

    @DBRef
    private UserRoles userRole;
   
    @DBRef
    private UserAccessKey userAccessKey;

    @JsonIgnore
    @CreatedDate
    private Date createdTime;

    @JsonIgnore
    @LastModifiedDate
    private Date updatedTime;
    
    private String createdBy;
    
    private String updatedBy;
    
    private List<BankAccount> bankAccounts = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return userAccessKey.getAccessKey();
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    // default values as "TRUE"
    // TODO : Need to implement account lock and deactivation.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !this.getStatus().equalsIgnoreCase("DEAD");
    }

    public void updateUser(User updateUser){
        if (Objects.nonNull(updateUser.getName()) && !Objects.equals(getName(), updateUser.getName())) {
           this.setName(updateUser.getName());
        }
        if (Objects.nonNull(updateUser.getStatus()) && !Objects.equals(getStatus(), updateUser.getStatus())) {
            this.setStatus(updateUser.getStatus());
        }
        if (Objects.nonNull(updateUser.getEmail()) && !Objects.equals(getEmail(), updateUser.getEmail())) {
            this.setEmail(updateUser.getEmail());
        }
        if (Objects.nonNull(updateUser.getBankAccounts())) {
            this.setBankAccounts(updateUser.getBankAccounts());
        }
    }
    
    /**
     * Adds a bank account to the user's list of bank accounts.
     * 
     * @param bankAccount The bank account to add
     */
    public void addBankAccount(BankAccount bankAccount) {
        if (this.bankAccounts == null) {
            this.bankAccounts = new ArrayList<>();
        }
        this.bankAccounts.add(bankAccount);
    }
    
    /**
     * Removes a bank account from the user's list of bank accounts.
     * 
     * @param bankAccountId The ID of the bank account to remove
     * @return true if the bank account was removed, false otherwise
     */
    public boolean removeBankAccount(String bankAccountId) {
        if (this.bankAccounts == null) {
            return false;
        }
        return this.bankAccounts.removeIf(account -> account.getId().equals(bankAccountId));
    }
    


}
