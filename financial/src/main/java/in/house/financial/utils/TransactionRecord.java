package in.house.financial.utils;

import lombok.Data;

import java.util.Date;

@Data
public class TransactionRecord {
    private Object recordDate;
    private Date transaction_date ;
    private String remark ;
    private Double withdraw ;
    private Double deposit ;
    private Object balance ;
    private String transactionType;
    private String category;
    private Boolean isUpiTransaction ;
    // New fields for AI-generated information
    private String aiGeneratedCategory; // AI-predicted category (e.g., food, petrol, travel)
    private String aiGeneratedDescription;
    private String aiGeneratedTransactionParty;
    private String transactionId; // Unique identifier for the transaction
    private String userId; // User ID to associate the transaction with a specific user
}
