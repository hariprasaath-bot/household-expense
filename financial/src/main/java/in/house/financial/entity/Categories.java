package in.house.financial.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "categories")
@Builder
@Data
public class Categories {
    @Id
    private String id;
    private String name;
    private boolean isCustom;
    private String categoryType; // Type of category (e.g., EXPENSE, INCOME, INVESTMENT)
    private String searchString; // String pattern to identify the category from transaction descriptions
    private String userId; // Associated user ID for custom categories, null for global categories
}
