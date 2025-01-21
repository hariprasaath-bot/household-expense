package in.house.financial.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "personel_accounts_table")
public class PersonelAccountsTable {

    @Id

    private String id;

   @Field(name = "user_id")
    private Long userId;

   @Field(name = "transaction_type")
    private String transactionType;

   @Field(name = "amount_value")
    private Double amountValue;

   @Field(name = "created_date")
    @JsonIgnore
    @CreatedDate
    private Date createdDate;

   @Field(name = "modified_date")
    @JsonIgnore
    @LastModifiedDate
    private Date modifiedDate;

}
