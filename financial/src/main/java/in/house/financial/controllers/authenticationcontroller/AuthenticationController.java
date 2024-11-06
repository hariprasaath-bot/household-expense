package in.house.financial.controllers.authenticationcontroller;

import com.google.gson.JsonObject;
import in.house.financial.interfaces.AuthenticationService;
import in.house.financial.securityconfig.securityDTO.SecurityDTO;
import in.house.financial.securityconfig.securityDTO.SignUpRequest;
import in.house.financial.securityconfig.securityDTO.SigninRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity<String> signUp(@RequestBody JsonObject request) {
        log.info(request.toString());
        return ResponseEntity.ok("success");
    }

    @GetMapping("/version")
    public ResponseEntity<String> getVersion() {
        return ResponseEntity.ok("0.1");
    }


}