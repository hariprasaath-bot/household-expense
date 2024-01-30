package in.house.financial.securityconfig;

import in.house.financial.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserSession {

    public UserDetails getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if(Objects.nonNull(userDetails)){
            return userDetails;
        }else {
            return null;
        }
    }
}
