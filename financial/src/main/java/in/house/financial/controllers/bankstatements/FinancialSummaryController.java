package in.house.financial.controllers.bankstatements;

import in.house.financial.interfaces.BankStatementService;
import in.house.financial.model.FinancialSummary;
import in.house.financial.securityconfig.UserSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/finance")
public class FinancialSummaryController {

    private final BankStatementService bankStatementService;
    private final UserSession userSession;

    /**
     * Create a new financial summary record
     */
    @PostMapping("/summary")
    public ResponseEntity<FinancialSummary> saveFinancialSummary(@RequestBody FinancialSummary financialSummary) {
        try {
            // If no user ID is provided, use the current logged-in user
            if (financialSummary.getUserId() == null || financialSummary.getUserId().isEmpty()) {
                String loggedInUserId = userSession.getUser().getId();
                financialSummary.setUserId(loggedInUserId);
            }

            // Set timestamp if not already set
            if (financialSummary.getTimestamp() == null) {
                financialSummary.setTimestamp(LocalDateTime.now());
            }

            FinancialSummary savedSummary = bankStatementService.saveOrUpdateFinancialSummary(financialSummary);
            return ResponseEntity.ok(savedSummary);
        } catch (Exception e) {
            log.error("Error saving financial summary: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get a specific financial summary by its identifier
     */
    @GetMapping("/summary/{summaryIdentifier}")
    public ResponseEntity<FinancialSummary> getFinancialSummaryByIdentifier(
            @PathVariable String summaryIdentifier,
            @RequestParam(required = false) String userId) {

        try {
            // If no user ID is provided, use the current logged-in user
            if (userId == null || userId.isEmpty()) {
                userId = userSession.getUser().getId();
            }

            FinancialSummary summary = bankStatementService.getFinancialSummaryByIdentifier(summaryIdentifier, userId);

            if (summary == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            log.error("Error retrieving financial summary by identifier: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Update an existing financial summary
     */
    @PutMapping("/summary/{summaryIdentifier}")
    public ResponseEntity<FinancialSummary> updateFinancialSummary(
            @PathVariable String summaryIdentifier,
            @RequestBody FinancialSummary financialSummary) {

        try {
            // If no user ID is provided, use the current logged-in user
            if (financialSummary.getUserId() == null || financialSummary.getUserId().isEmpty()) {
                String loggedInUserId = userSession.getUser().getId();
                financialSummary.setUserId(loggedInUserId);
            }

            // Make sure the identifier in the path is used
            financialSummary.setSummaryIdentifier(summaryIdentifier);

            // Check if summary exists first
            FinancialSummary existingSummary = bankStatementService.getFinancialSummaryByIdentifier(
                    summaryIdentifier, financialSummary.getUserId());

            if (existingSummary == null) {
                return ResponseEntity.notFound().build();
            }

            // Keep the original ID to ensure we update the right document
            financialSummary.setId(existingSummary.getId());

            // Set timestamp if not already set
            if (financialSummary.getTimestamp() == null) {
                financialSummary.setTimestamp(LocalDateTime.now());
            }

            FinancialSummary updatedSummary = bankStatementService.saveOrUpdateFinancialSummary(financialSummary);
            return ResponseEntity.ok(updatedSummary);
        } catch (Exception e) {
            log.error("Error updating financial summary: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/summary")
    public ResponseEntity<List<FinancialSummary>> getFinancialSummary(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        try {
            // If no user ID is provided, use the current logged-in user
            if (userId == null || userId.isEmpty()) {
                userId = userSession.getUser().getId();
            }

            List<FinancialSummary> summaries = bankStatementService.getFinancialSummaryByUserIdAndTimeRange(
                    userId, startTime, endTime);

            return ResponseEntity.ok(summaries);
        } catch (Exception e) {
            log.error("Error retrieving financial summaries: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
