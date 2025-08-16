package in.house.financial.controllers.authenticationcontroller;

import com.google.gson.JsonObject;
import in.house.financial.interfaces.AuthenticationService;
import in.house.financial.securityconfig.exception.InvalidTokenException;
import in.house.financial.securityconfig.securityDTO.SecurityDTO;
import in.house.financial.securityconfig.securityDTO.SignUpRequest;
import in.house.financial.securityconfig.securityDTO.SigninRequest;
import in.house.financial.securityconfig.securityDTO.TokenRefreshRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;



    @PostMapping("/signin")
    public ResponseEntity<SecurityDTO> signin(@RequestBody SigninRequest request) {
        return ResponseEntity.ok(authenticationService.signin(request));
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> signUp(@RequestBody SignUpRequest request) {
        log.info(request.toString());
        return ResponseEntity.ok(authenticationService.signup(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        try {
            return authenticationService.refreshToken(request);
        } catch (InvalidTokenException e) {
            log.error("Token refresh failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid token", "message", e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error during token refresh: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Server error", "message", "An unexpected error occurred"));
        }
    }

    @GetMapping("/version")
    public ResponseEntity<String> getVersion() {
        return ResponseEntity.ok("0.1");
    }


}