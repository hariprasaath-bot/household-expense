package in.house.financial.controllers.bankstatements;

import in.house.financial.dto.CategoryRequest;
import in.house.financial.entity.Categories;
import in.house.financial.interfaces.BankStatementService;
import in.house.financial.securityconfig.UserSession;
import in.house.financial.utils.ServerSentEventUtil;
import in.house.financial.utils.TransactionRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/bank")
public class BankStatementController {

    private final BankStatementService bankStatementService;
    private final ServerSentEventUtil severSentEventUtil;
    private final UserSession userSession;



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
    public ResponseEntity<Long> uploadBankStatement(
            @RequestParam("type") String bank,
            @RequestParam("file") MultipartFile uploadedStatement) {
        long uniqueEmitterId = System.currentTimeMillis();
        bankStatementService.processStatementFile(bank, uploadedStatement, uniqueEmitterId);
        return ResponseEntity.ok(uniqueEmitterId);
    }

    @GetMapping("/statement/progress/{uniqueEmitterId}")
    public SseEmitter getUploadProgress(@PathVariable long uniqueEmitterId) {
        return severSentEventUtil.getProgressEmitter(uniqueEmitterId);
    }

    /**
     * Retrieves bank transactions for the current user with optional date filtering
     * @param startDate Optional start date for filtering transactions
     * @param endDate Optional end date for filtering transactions
     * @return List of transaction records matching the criteria
     */
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionRecord>> getBankTransactions(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            String userId = userSession.getUser().getId();
            List<TransactionRecord> transactions = bankStatementService.getBankTransactionsByUserIdAndDateRange(
                    userId, startDate, endDate);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            log.error("Exception occurred while retrieving bank transactions: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Updates a list of bank transaction records
     * @param transactionRecords List of transaction records to update
     * @return Number of records updated
     */
    @PutMapping("/transactions")
    public ResponseEntity<Integer> updateBankTransactions(@RequestBody List<TransactionRecord> transactionRecords) {
        try {
            String userId = userSession.getUser().getId();

            // Ensure all transactions belong to the current user
            for (TransactionRecord record : transactionRecords) {
                if (record.getUserId() == null || !record.getUserId().equals(userId)) {
                    record.setUserId(userId);
                }
            }

            int updatedCount = bankStatementService.updateBankTransactions(transactionRecords);
            return ResponseEntity.ok(updatedCount);
        } catch (Exception e) {
            log.error("Exception occurred while updating bank transactions: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Categories> > getBankCategories() {
        try {
            List<Categories>  categories = bankStatementService.getBankCategories();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            log.error("Exception occurred while retrieving bank categories: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
