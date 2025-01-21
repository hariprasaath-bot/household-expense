package in.house.financial.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@Data
@Document(collection = "inmates")
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    private String id;
    
    private String name;
    
    @NonNull
    private String email;
    
    private String status;

    @DBRef
    private UserRoles userRole;
   
    @DBRef
    private UserAccessKey userAccessKey;

    @JsonIgnore
    @CreatedDate
    private Date createdTime;

    @JsonIgnore
    @LastModifiedDate
    private Date updatedTime;
    
    private String createdBy;
    
    private String updatedBy;

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
        return !this.getStatus().equalsIgnoreCase("DEAD");
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
