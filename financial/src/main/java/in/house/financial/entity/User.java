package in.house.financial.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@Entity
@Table(name="inmates")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;
    @Column
    private String email;
    @Column
    private String status;
    @OneToOne
    @JoinColumn(name="user_access_key_mapping_id")
    private UserAcessKey userAcessKey;
    @Column
    @JsonIgnore
    @CreationTimestamp
    private Date createdTime;
    @Column
    @JsonIgnore
    @UpdateTimestamp
    private Date updatedTime;

}
