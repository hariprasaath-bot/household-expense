package in.house.financial.interfaces;

import in.house.financial.dto.CategoryRequest;
import in.house.financial.entity.Categories;
import in.house.financial.model.FinancialSummary;
import in.house.financial.utils.TransactionRecord;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface BankStatementService {

    String readStatementFile(String path) throws IOException;

    void processStatementFile(String bank, MultipartFile uploadedStatementFile, long uniqueEmitterId);

    FinancialSummary saveFinancialSummary(FinancialSummary financialSummary);

    /**
     * Saves or updates a financial summary based on summaryIdentifier
     * @param financialSummary The financial summary to save or update
     * @return The saved or updated financial summary
     */
    FinancialSummary saveOrUpdateFinancialSummary(FinancialSummary financialSummary);

    /**
     * Retrieves a financial summary by its identifier
     * @param summaryIdentifier The unique identifier for the summary
     * @param userId The user ID
     * @return The financial summary if found, null otherwise
     */
    FinancialSummary getFinancialSummaryByIdentifier(String summaryIdentifier, String userId);

    List<FinancialSummary> getFinancialSummaryByUserIdAndTimeRange(String userId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * Retrieves bank transactions by user ID and optional date range
     * @param userId The user ID
     * @param startDate Optional start date for filtering transactions
     * @param endDate Optional end date for filtering transactions
     * @return List of transaction records matching the criteria
     */
    List<TransactionRecord> getBankTransactionsByUserIdAndDateRange(String userId, Date startDate, Date endDate);

    /**
     * Updates a list of bank transaction records
     * @param transactionRecords List of transaction records to update
     * @return Number of records updated
     */
    int updateBankTransactions(List<TransactionRecord> transactionRecords);

    List<Categories>  getBankCategories();
}
