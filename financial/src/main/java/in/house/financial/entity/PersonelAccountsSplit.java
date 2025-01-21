package in.house.financial.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;


@Data
@Document(collection = "personel_accounts_split")
public class PersonelAccountsSplit {

    @Id
    private String id;

    @DBRef
    private PersonelAccountsTable personelAccountsTable;

   @Field(name = "user_id")
    private Long userId;

   @Field(name = "transaction_type")
    private String transactionType;

   @Field(name = "amount_value")
    private Double amountValue;


}
