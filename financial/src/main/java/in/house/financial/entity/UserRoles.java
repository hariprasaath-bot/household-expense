package in.house.financial.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "user_roles") // Specify the MongoDB collection name
@NoArgsConstructor
@AllArgsConstructor
public class UserRoles {
    @Id
    private String id; // Use String for MongoDB's ObjectId
    private String name;
}
