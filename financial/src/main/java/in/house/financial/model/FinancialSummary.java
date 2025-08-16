package in.house.financial.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "financial_summary")
public class FinancialSummary {

    @Id
    private String id;
    private String userId;
    private String summaryIdentifier; // Unique identifier for the financial summary for upsert operations
    private String bankBalance;
    private String kiteWallet;
    private double dcxWallet;
    private String kitePosition;
    private String kiteHolding;
    private String dcxPosition;
    private String rupayCardBill;
    private String amazonCardBill;
    private double expense;
    private String earning;
    private double loss;
    private List<LossEntry> lossEntries;
    private LocalDateTime timestamp;

    @Data
    public static class LossEntry {
        private String category;
        private double value;
    }
}
