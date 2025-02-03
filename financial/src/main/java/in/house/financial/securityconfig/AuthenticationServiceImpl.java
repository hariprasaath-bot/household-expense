package in.house.financial.securityconfig;

import in.house.financial.interfaces.AuthenticationService;
import in.house.financial.interfaces.JwtService;
import in.house.financial.interfaces.UserInterface;
import in.house.financial.repository.UserRepository;
import in.house.financial.securityconfig.exception.InvalidTokenException;
import in.house.financial.securityconfig.securityDTO.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserAuthServiceImpl userDetailsService;
    private final UserInterface userServices;

    @Override
    public SecurityDTO signin(SigninRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var jwt = jwtService.generateToken(user);
        return SecurityDTO.builder().email(user.getEmail()).userId(user.getName()).accessToken(jwt).refreshToken(jwtService.generateRefreshToken(user)).build();
    }

    @Override
    public ResponseEntity<Object> signup(SignUpRequest request) {
        return userServices.createUser(request);
    }

    public ResponseEntity<?> refreshToken(TokenRefreshRequest request) throws InvalidTokenException {
        String refreshToken = request.getRefreshToken();

        // Extract username from refresh token
        String username = jwtService.extractUserName(refreshToken);
        UserDetails userDetails = userDetailsService.userDetailsService().loadUserByUsername(username);

        // Validate refresh token
        if (!jwtService.isRefreshTokenValid(refreshToken, userDetails)) {
            throw new InvalidTokenException("Invalid refresh token");
        }

        // Generate new access token
        String newAccessToken = jwtService.generateAccessToken(userDetails);

        return ResponseEntity.ok(TokenRefreshResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)  // Return same refresh token
                .tokenType("Bearer")
                .build());
    }

}
