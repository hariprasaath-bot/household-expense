package in.house.financial.interfaces;

import in.house.financial.securityconfig.securityDTO.SecurityDTO;
import in.house.financial.securityconfig.securityDTO.SignUpRequest;
import in.house.financial.securityconfig.securityDTO.SigninRequest;

public interface AuthenticationService {
    SecurityDTO signin(SigninRequest request);
}
