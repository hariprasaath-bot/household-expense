package in.house.financial.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "household_accounts_table")
public class HouseholdAccountsTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "amount_value")
    private Double amountValue;

    @Column(name = "accepted_status")
    private String acceptedStatus;

    @Column(name = "household_accounts_split_id")
    private Long householdAccountsSplitId;

    @Column(name = "created_date")
    @JsonIgnore
    @CreationTimestamp
    private Date createdDate;

    @Column(name = "modified_date")
    @JsonIgnore
    @UpdateTimestamp
    private Date modifiedDate;

    @OneToMany(mappedBy = "householdAccountsTable")
    private List<HouseholdAccountsSplit> householdAccountsSplits;

}
