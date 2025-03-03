package in.house.financial.controllers.bankstatements;

import in.house.financial.interfaces.BankStatementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
@RequestMapping("/bank")
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

    @PostMapping("/statement/upload")
    public ResponseEntity<SseEmitter> uploadBankStatement(@RequestParam("type") String bank, @RequestParam("file") MultipartFile uploadedStatment){
        SseEmitter emitter = new SseEmitter();
        CompletableFuture.runAsync(() -> {
            try {
                bankStatementService.parseUploadedStatementFile(bank, uploadedStatment, emitter).complete();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return ResponseEntity.ok(emitter);
    }
}
