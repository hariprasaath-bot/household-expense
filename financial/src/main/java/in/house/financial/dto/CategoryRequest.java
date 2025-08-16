package in.house.financial.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for category operations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {
    private String name;
    private String categoryType; // Type of category (e.g., EXPENSE, INCOME, INVESTMENT)
    private String searchString; // String pattern to identify the category from transaction descriptions
}