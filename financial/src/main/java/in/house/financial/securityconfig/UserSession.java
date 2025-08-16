package in.house.financial.securityconfig;

import in.house.financial.entity.User;
import in.house.financial.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class UserSession {

    @Autowired
    private UserRepository userRepository;

    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findByEmail(userDetails.getUsername());
        if (user.isPresent()) {
            log.info("logged in user information :: {}", userDetails.getUsername());
            return user.get();
        } else {
            log.warn("User not found for email :: {}", userDetails.getUsername());
            throw new UsernameNotFoundException("user details not found");
        }
    }
}
