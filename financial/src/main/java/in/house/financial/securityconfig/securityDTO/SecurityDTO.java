package in.house.financial.securityconfig.securityDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SecurityDTO {
    private String accessToken;
    private String refreshToken;
    private String userId;
    private String email;
}