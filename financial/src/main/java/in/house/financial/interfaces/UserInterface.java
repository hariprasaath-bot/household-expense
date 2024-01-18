package in.house.financial.interfaces;

import in.house.financial.entity.User;
import in.house.financial.securityconfig.securityDTO.SignUpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


public interface UserInterface {

    public ResponseEntity<Object> createUser(SignUpRequest user);
    public ResponseEntity<String> updateUser(User user);
    public ResponseEntity<String> deleteUser(Integer cuid);
    public ResponseEntity<String> deactivateUser(Integer cuid);

}

