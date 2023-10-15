package in.house.financial.interfaces;

import in.house.financial.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public interface UserService {

    public ResponseEntity<String> createUser(User user);
    public ResponseEntity<String> updateUser(User user);
    public ResponseEntity<String> deleteUser(Integer cuid);
    public ResponseEntity<String> deactivateUser(Integer cuid);

}

