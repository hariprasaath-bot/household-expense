package in.house.financial.controllers.usercontroller;

import in.house.financial.entity.User;
import in.house.financial.interfaces.UserInterface;
import in.house.financial.securityconfig.securityDTO.SignUpRequest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Data
@RequestMapping("/user")
public class UserController {

    private final UserInterface userServices;
    @PostMapping("/create")
    public ResponseEntity<Object> createUser(@RequestBody SignUpRequest user){
        return userServices.createUser(user);
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody User user){
        return userServices.updateUser(user);
    }

    @DeleteMapping("/delete/{cuid}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer cuid){
        return userServices.deleteUser(cuid);
    }

    @GetMapping("/deactivate/{cuid}")
    public ResponseEntity<String> deactivateUser(@PathVariable Integer cuid){
        return userServices.deactivateUser(cuid);
    }
}
