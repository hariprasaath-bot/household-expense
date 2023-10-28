package in.house.financial.usercontroller;

import in.house.financial.entity.User;
import in.house.financial.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userServices;
    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody User user){
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
