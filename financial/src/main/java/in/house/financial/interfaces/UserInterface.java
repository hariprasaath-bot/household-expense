package in.house.financial.interfaces;

import in.house.financial.dto.BankAccountRequest;
import in.house.financial.dto.CategoryRequest;
import in.house.financial.dto.UserProfileDTO;
import in.house.financial.entity.User;
import in.house.financial.securityconfig.securityDTO.SignUpRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface UserInterface {

    ResponseEntity<Object> createUser(SignUpRequest user);
    ResponseEntity<String> updateUser(User user);
    ResponseEntity<String> deleteUser(String cuid);
    ResponseEntity<String> deactivateUser(String cuid);
    
    /**
     * Gets the user profile for the current authenticated user.
     * 
     * @return The user profile
     */
    ResponseEntity<UserProfileDTO> getUserProfile();
    
    /**
     * Gets the user profile for the specified user ID.
     * 
     * @param userId The user ID
     * @return The user profile
     */
    ResponseEntity<UserProfileDTO> getUserProfileById(String userId);
    
    /**
     * Updates the user profile for the current authenticated user.
     * 
     * @param userProfile The updated user profile
     * @return A response indicating success or failure
     */
    ResponseEntity<String> updateUserProfile(UserProfileDTO userProfile);
    
    /**
     * Adds a bank account to the current authenticated user.
     * 
     * @param bankAccountRequest The bank account to add
     * @return A response indicating success or failure
     */
    ResponseEntity<String> addBankAccount(BankAccountRequest bankAccountRequest);
    
    /**
     * Removes a bank account from the current authenticated user.
     * 
     * @param bankAccountId The ID of the bank account to remove
     * @return A response indicating success or failure
     */
    ResponseEntity<String> removeBankAccount(String bankAccountId);
    
    /**
     * Adds a category to the current authenticated user.
     * 
     * @param categoryRequest The category to add
     * @return A response indicating success or failure
     */
    ResponseEntity<String> addCategory(List<CategoryRequest> categoryRequest);
    
    /**
     * Removes a category from the current authenticated user.
     * Only custom categories can be removed.
     * 
     * @param categoryId The ID of the category to remove
     * @return A response indicating success or failure
     */
    ResponseEntity<String> removeCategory(String categoryId);
}

