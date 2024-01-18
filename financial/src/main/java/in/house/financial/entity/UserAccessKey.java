package in.house.financial.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="user_access_key_mapping")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAccessKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="access_key")
    private String accessKey;
}

