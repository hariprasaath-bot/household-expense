package in.house.financial.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@Data
@Entity
@Builder
@Table(name="inmates")
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;
    @Column
    @NonNull
    private String email;
    @Column
    private String status;


    @ManyToOne
    @JoinColumn(name = "user_role_id")
    private UserRoles userRole;

    @OneToOne
    @JoinColumn(name="user_access_key_mapping_id",referencedColumnName = "id")
    private UserAccessKey userAccessKey;

    @Column
    @JsonIgnore
    @CreationTimestamp
    private Date createdTime;

    @Column
    @JsonIgnore
    @UpdateTimestamp
    private Date updatedTime;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return userAccessKey.getAccessKey();
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    // default values as "TRUE"
    // TODO : Need to implement account lock and deactivation.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void updateUser(User updateUser){
        if (Objects.nonNull(updateUser.getName()) && !Objects.equals(getName(), updateUser.getName())) {
           this.setName(updateUser.getName());
        }
        if (Objects.nonNull(updateUser.getStatus()) && !Objects.equals(getStatus(), updateUser.getStatus())) {
            this.setName(updateUser.getStatus());
        }

    }
}
