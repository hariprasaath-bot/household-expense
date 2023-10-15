package in.house.financial.services;

import in.house.financial.entity.User;
import in.house.financial.interfaces.UserService;
import in.house.financial.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
public class UserServicesImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<String> createUser(User user){
        ResponseEntity<String> reponse ;
        try {
            if (Objects.isNull(userRepository.findByEmail(user.getEmail()))) {
                userRepository.save(user);
                reponse = new ResponseEntity<>("Created successfully", HttpStatus.CREATED);
            } else {
                reponse = new ResponseEntity<>("User Already Exists", HttpStatus.OK);
            }
        }catch (Exception e){
            log.info("Exception occurred in creating user (USER MAY BE DEAD) {}",e.getMessage());
            reponse = new ResponseEntity<>("Exception occurred in creating user (USER MAY BE DEAD)", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return reponse;
    }

    public ResponseEntity<String> updateUser(User user){
        ResponseEntity<String> reponse ;
        try {
            User existingUser = userRepository.findByEmail(user.getEmail());
            if (Objects.isNull(existingUser)) {
                reponse = new ResponseEntity<>("User Doesn't Exists", HttpStatus.OK);
            } else {
                existingUser.setName(user.getName());
                existingUser.setEmail(user.getEmail());
                existingUser.setStatus(user.getStatus());
                userRepository.save(existingUser);
                reponse = new ResponseEntity<>("updated successfully", HttpStatus.CREATED);
            }
        }catch (Exception e){
            log.info("Exception occurred in updating user (USER MAY BE DEAD) {}",e.getMessage());
            reponse = new ResponseEntity<>("Exception occurred in updating user (USER MAY BE DEAD)", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return reponse;
    }

    public ResponseEntity<String> deleteUser(Integer cuid){
        ResponseEntity<String> reponse ;
        try {
            Optional<User> existingUser = userRepository.findById(cuid);
            if (!existingUser.isPresent()) {
                reponse = new ResponseEntity<>("User Doesn't Exists", HttpStatus.OK);
            } else {
                User user = existingUser.get();
                userRepository.deleteById(user.getId());
                reponse = new ResponseEntity<>("deleted successfully", HttpStatus.CREATED);
            }
        }catch (Exception e){
            log.info("Exception occurred in deleting user (USER MAY BE DEAD) {}",e.getMessage());
            reponse = new ResponseEntity<>("Exception occurred in deleting user (USER MAY BE DEAD)", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return reponse;
    }

    public ResponseEntity<String> deactivateUser(Integer cuid){
        ResponseEntity<String> reponse ;
        try {
            Optional<User> existingUser = userRepository.findById(cuid);
            if (!existingUser.isPresent()) {
                reponse = new ResponseEntity<>("User Doesn't Exists", HttpStatus.OK);
            } else {
                User user = existingUser.get();
                user.setStatus("DEAD");
                userRepository.save(user);
                reponse = new ResponseEntity<>("deactivated successfully", HttpStatus.CREATED);
            }
        }catch (Exception e){
            log.info("Exception occurred in deactivating user (USER MAY BE DEAD) {}",e.getMessage());
            reponse = new ResponseEntity<>("Exception occurred in deactivating user (USER MAY BE DEAD)", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return reponse;
    }


}
