package in.house.financial.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "transaction_type_master")
@Data
public class TransactionTypeMaster {

    @Id
    private String id;

   @Field(name = "transaction_type_name")
    private String transactionTypeName;

    // Getters and setters
}
