package in.house.financial.entity;

import jakarta.persistence.*;
import lombok.Data;

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

    @OneToOne
    @JoinColumn(name = "inmates_id")
    private User user;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "amount_value")
    private Double amountValue;

}
