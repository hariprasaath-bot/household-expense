package in.house.financial.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "accepted_status_master")
public class AcceptedStatusMaster {

    @Id
    private Long id;

    @Column(name = "accepted_status_master_name")
    private String acceptedStatusMasterName;

}
