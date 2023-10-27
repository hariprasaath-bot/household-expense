package in.house.financial.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "transaction_type_master")
public class TransactionTypeMaster {

    @Id
    private Long id;

    @Column(name = "transaction_type_name")
    private String transactionTypeName;

    // Getters and setters
}
