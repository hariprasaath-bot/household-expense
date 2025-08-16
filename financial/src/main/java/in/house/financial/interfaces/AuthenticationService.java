package in.house.financial.interfaces;

import in.house.financial.securityconfig.exception.InvalidTokenException;
import in.house.financial.securityconfig.securityDTO.SecurityDTO;
import in.house.financial.securityconfig.securityDTO.SignUpRequest;
import in.house.financial.securityconfig.securityDTO.SigninRequest;
import in.house.financial.securityconfig.securityDTO.TokenRefreshRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    SecurityDTO signin(SigninRequest request);

    ResponseEntity<Object> signup(SignUpRequest request);

    ResponseEntity<?> refreshToken(TokenRefreshRequest request) throws InvalidTokenException;

}
