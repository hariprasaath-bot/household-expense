package in.house.financial.interfaces;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String extractUserName(String token);
    String generateToken(UserDetails userDetails);
    String generateAccessToken(UserDetails userDetails);
    String generateRefreshToken(UserDetails userDetails);
    boolean isRefreshTokenValid(String token, UserDetails userDetails);
    boolean isTokenValid(String token, UserDetails userDetails);
}
