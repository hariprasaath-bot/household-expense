package in.house.financial.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;


@Document(collection = "household_accounts_table")
@Data
public class HouseholdAccountsTable {

    @Id
    private String id;

    @DBRef
    private User user;

   @Field(name = "transaction_type")
    private String transactionType;

   @Field(name = "amount_value")
    private Double amountValue;

   @Field(name = "accepted_status")
    private String acceptedStatus;

   @Field(name = "created_date")
    @JsonIgnore
    @CreatedDate
    private Date createdDate;

    @Field(name = "modified_date")
    @JsonIgnore
    @LastModifiedDate
    private Date modifiedDate;

    @DBRef
    private List<HouseholdAccountsSplit> householdAccountsSplits;


}
