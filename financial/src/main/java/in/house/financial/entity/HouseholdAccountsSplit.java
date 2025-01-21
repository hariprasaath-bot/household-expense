package in.house.financial.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Data
@Document(collection = "household_accounts_split")
public class HouseholdAccountsSplit {

    @Id
    private String id;

    @DBRef
    private HouseholdAccountsTable householdAccountsTable;

    @DBRef
    private User user;

   @Field(name = "transaction_type")
    private String transactionType;

   @Field(name = "amount_value")
    private Double amountValue;

}
