package in.house.financial.services;

import in.house.financial.entity.User;
import in.house.financial.entity.UserAccessKey;
import in.house.financial.interfaces.JwtService;
import in.house.financial.interfaces.UserInterface;
import in.house.financial.repository.UserAccessKeyRepository;
import in.house.financial.repository.UserRepository;
import in.house.financial.securityconfig.UserSession;
import in.house.financial.securityconfig.securityDTO.SecurityDTO;
import in.house.financial.securityconfig.securityDTO.SignUpRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServicesImpl implements UserInterface {

    private final UserRepository userRepository;
    private final UserAccessKeyRepository userAccessKeyRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserSession userSession;


    public ResponseEntity<Object> createUser(SignUpRequest request) {
        ResponseEntity<Object> response;
        try {
            Optional<User> userData = userRepository.findByEmail(request.getEmail());
            if (userData.isEmpty()) {
                // Create UserAccessKey
                UserAccessKey userAccessKey = UserAccessKey.builder()
                        .accessKey(passwordEncoder.encode(request.getPassword()))
                        .build();
                // Save UserAccessKey
                userAccessKey = userAccessKeyRepo.save(userAccessKey);
                // Create User and associate UserAccessKey
                var user = User.builder()
                        .name(request.getName())
                        .email(request.getEmail())
                        .status("active")
                        .userAccessKey(userAccessKey)
                        .createdBy("admin")
                        .updatedBy("admin")
                        .build();
                // Save User along with UserAccessKey
                userRepository.save(user);

                // Generate token and create SecurityDTO
                var jwt = jwtService.generateToken(user);
                SecurityDTO token = SecurityDTO.builder().userId(user.getName()).email(user.getEmail()).accessToken(jwt).refreshToken(jwtService.generateRefreshToken(user)).build();

                response = ResponseEntity.ok(token);
            } else {
                response = new ResponseEntity<>("User Already Exists", HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("Exception occurred in creating user (USER MAY BE DEAD) {}", e.getMessage());
            response = new ResponseEntity<>("Exception occurred in creating user (USER MAY BE DEAD)", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }


    public ResponseEntity<String> updateUser(User user) {
        ResponseEntity<String> reponse;
        try {

            Optional<User> userData = userRepository.findByEmail(user.getEmail());
            if (userData.isEmpty()) {
                reponse = new ResponseEntity<>("User Doesn't Exists", HttpStatus.OK);
            } else {
                User existingUser = userData.get();
                existingUser.updateUser(user);
                existingUser.setUpdatedBy(userSession.getUser().getUsername());
                userRepository.save(existingUser);
                reponse = new ResponseEntity<>("updated successfully", HttpStatus.CREATED);
            }
        } catch (Exception e) {
            log.error("Exception occurred in updating user (USER MAY BE DEAD) {}", e.getMessage());
            reponse = new ResponseEntity<>("Exception occurred in updating user (USER MAY BE DEAD)", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return reponse;
    }

    public ResponseEntity<String> deleteUser(String cuid) {
        ResponseEntity<String> reponse;
        try {
            Optional<User> existingUser = userRepository.findById(cuid);
            if (existingUser.isEmpty()) {
                reponse = new ResponseEntity<>("User Doesn't Exists", HttpStatus.OK);
            } else {
                User user = existingUser.get();
                userRepository.deleteById(user.getId());
                reponse = new ResponseEntity<>("deleted successfully", HttpStatus.CREATED);
            }
        } catch (Exception e) {
            log.error("Exception occurred in deleting user (USER MAY BE DEAD) {}", e.getMessage());
            reponse = new ResponseEntity<>("Exception occurred in deleting user (USER MAY BE DEAD)", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return reponse;
    }

    public ResponseEntity<String> deactivateUser(String cuid) {
        ResponseEntity<String> reponse;
        try {
            Optional<User> existingUser = userRepository.findById(cuid);
            if (existingUser.isEmpty()) {
                reponse = new ResponseEntity<>("User Doesn't Exists", HttpStatus.OK);
            } else {
                User user = existingUser.get();
                user.setStatus("DEAD");
                user.setUpdatedBy(userSession.getUser().getUsername());
                userRepository.save(user);
                reponse = new ResponseEntity<>("deactivated successfully", HttpStatus.CREATED);
            }
        } catch (Exception e) {
            log.error("Exception occurred in deactivating user (USER MAY BE DEAD) {}", e.getMessage());
            reponse = new ResponseEntity<>("Exception occurred in deactivating user (USER MAY BE DEAD)", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return reponse;
    }


}
