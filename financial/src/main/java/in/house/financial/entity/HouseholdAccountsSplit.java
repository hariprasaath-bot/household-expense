package in.house.financial.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "household_accounts_split")
public class HouseholdAccountsSplit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "household_accounts_table_id")
    private HouseholdAccountsTable householdAccountsTable;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "amount_value")
    private Double amountValue;

}
