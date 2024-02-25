package in.house.financial.controllers.bankstatements;

import in.house.financial.interfaces.BankStatementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class BankStatementController {

    @Autowired
    private BankStatementService bankStatementService;


    @PostMapping("/statement")
    public ResponseEntity<String> addBankStatement( @RequestBody String path){
        try {
            String result = bankStatementService.readStatementFile(path);
            return ResponseEntity.ok(result);
        }catch (Exception e){
            log.error("Exception occurred in reading bank statements : {}",e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
