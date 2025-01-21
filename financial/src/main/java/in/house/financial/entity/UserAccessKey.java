package in.house.financial.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "user_access_keys") // Specify the MongoDB collection name
@NoArgsConstructor
@AllArgsConstructor
public class UserAccessKey {
    @Id
    private String id; // Use String for MongoDB's ObjectId
    private String accessKey;
}
