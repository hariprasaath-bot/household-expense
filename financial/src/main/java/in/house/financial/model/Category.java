package in.house.financial.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * Model class representing a transaction category.
 * Categories can be default (system-defined) or custom (user-defined).
 * Each category has a type and search string to help identify transactions.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    private String id;
    private String name;
    private boolean isCustom;
    private String categoryType; // Type of category (e.g., EXPENSE, INCOME, INVESTMENT)
    private String searchString; // String pattern to identify the category from transaction descriptions
    private String userId; // Associated user ID for custom categories, null for global categories
}