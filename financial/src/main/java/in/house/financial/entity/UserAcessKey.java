package in.house.financial.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="user_access_key_mapping")
@Data
public class UserAcessKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "inmates_id")
    private String accessKey;
}
