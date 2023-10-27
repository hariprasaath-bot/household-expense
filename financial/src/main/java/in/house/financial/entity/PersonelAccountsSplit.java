package in.house.financial.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "personel_accounts_split")
public class PersonelAccountsSplit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "personel_accounts_table_id")
    private PersonelAccountsTable personelAccountsTable;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "amount_value")
    private Double amountValue;


}
