package in.house.financial.controllers.usercontroller;

import in.house.financial.dto.BankAccountRequest;
import in.house.financial.dto.CategoryRequest;
import in.house.financial.dto.UserProfileDTO;
import in.house.financial.entity.User;
import in.house.financial.interfaces.UserInterface;
import in.house.financial.securityconfig.securityDTO.SignUpRequest;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for user management operations.
 */
@RestController
@Data
@RequestMapping("/user")
public class UserController {

    private final UserInterface userServices;

    /**
     * Creates a new user.
     * 
     * @param user The user to create
     * @return The created user
     */
    @PostMapping("/create")
    public ResponseEntity<Object> createUser(@RequestBody SignUpRequest user) {
        return userServices.createUser(user);
    }

    /**
     * Updates an existing user.
     * 
     * @param user The user to update
     * @return A response indicating success or failure
     */
    @PostMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody User user) {
        return userServices.updateUser(user);
    }

    /**
     * Deletes a user.
     * 
     * @param cuid The user ID
     * @return A response indicating success or failure
     */
    @DeleteMapping("/delete/{cuid}")
    public ResponseEntity<String> deleteUser(@PathVariable String cuid) {
        return userServices.deleteUser(cuid);
    }

    /**
     * Deactivates a user.
     * 
     * @param cuid The user ID
     * @return A response indicating success or failure
     */
    @GetMapping("/deactivate/{cuid}")
    public ResponseEntity<String> deactivateUser(@PathVariable String cuid) {
        return userServices.deactivateUser(cuid);
    }
    
    /**
     * Gets the profile for the current authenticated user.
     * 
     * @return The user profile
     */
    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getUserProfile() {
        return userServices.getUserProfile();
    }
    
    /**
     * Gets the profile for a specific user.
     * 
     * @param userId The user ID
     * @return The user profile
     */
    @GetMapping("/profile/{userId}")
    public ResponseEntity<UserProfileDTO> getUserProfileById(@PathVariable String userId) {
        return userServices.getUserProfileById(userId);
    }
    
    /**
     * Updates the profile for the current authenticated user.
     * 
     * @param userProfile The updated user profile
     * @return A response indicating success or failure
     */
    @PostMapping("/profile/update")
    public ResponseEntity<String> updateUserProfile(@RequestBody UserProfileDTO userProfile) {
        return userServices.updateUserProfile(userProfile);
    }
    
    /**
     * Adds a bank account to the current authenticated user.
     * 
     * @param bankAccountRequest The bank account to add
     * @return A response indicating success or failure
     */
    @PostMapping("/bank-account/add")
    public ResponseEntity<String> addBankAccount(@RequestBody BankAccountRequest bankAccountRequest) {
        return userServices.addBankAccount(bankAccountRequest);
    }
    
    /**
     * Removes a bank account from the current authenticated user.
     * 
     * @param bankAccountId The ID of the bank account to remove
     * @return A response indicating success or failure
     */
    @DeleteMapping("/bank-account/remove/{bankAccountId}")
    public ResponseEntity<String> removeBankAccount(@PathVariable String bankAccountId) {
        return userServices.removeBankAccount(bankAccountId);
    }
    
    /**
     * Adds a category to the current authenticated user.
     * The category includes a type and search string for transaction identification.
     * 
     * @param categoryRequest The category to add with name, type and search string
     * @return A response indicating success or failure
     */
    @PostMapping("/category/add")
    public ResponseEntity<String> addCategory(@RequestBody List<CategoryRequest> categoryRequest) {
        return userServices.addCategory(categoryRequest);
    }
    
    /**
     * Removes a category from the current authenticated user.
     * Only custom categories can be removed.
     * 
     * @param categoryId The ID of the category to remove
     * @return A response indicating success or failure
     */
    @DeleteMapping("/category/remove/{categoryId}")
    public ResponseEntity<String> removeCategory(@PathVariable String categoryId) {
        return userServices.removeCategory(categoryId);
    }
}
