package in.house.financial.interfaces;

import in.house.financial.entity.User;
import in.house.financial.securityconfig.securityDTO.SignUpRequest;
import org.springframework.http.ResponseEntity;


public interface UserInterface {

    ResponseEntity<Object> createUser(SignUpRequest user);
    ResponseEntity<String> updateUser(User user);
    ResponseEntity<String> deleteUser(String cuid);
    ResponseEntity<String> deactivateUser(String cuid);

}

