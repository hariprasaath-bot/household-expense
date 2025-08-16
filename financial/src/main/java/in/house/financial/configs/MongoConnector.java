package in.house.financial.configs;

import in.house.financial.model.FinancialSummary;
import in.house.financial.utils.TransactionRecord;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class MongoConnector {

    private final MongoOperations mongoOperations;

    public Object putDataInMongo(List<TransactionRecord> data, String collectionName) {
        return mongoOperations.insert(data, collectionName);
    }

    public void upsertRecords(List<TransactionRecord> records, String collectionName) {
        for (TransactionRecord record : records) {
            Document query = new Document("transactionId", record.getTransactionId());
            Document update = new Document("$set", new Document()
                    .append("recordDate", record.getRecordDate())
                    .append("transaction_date", record.getTransaction_date())
                    .append("remark", record.getRemark())
                    .append("withdraw", record.getWithdraw())
                    .append("deposit", record.getDeposit())
                    .append("balance", record.getBalance())
                    .append("transactionType", record.getTransactionType())
                    .append("category", record.getCategory())
                    .append("isUpiTransaction", record.getIsUpiTransaction())
                    .append("aiGeneratedCategory", record.getAiGeneratedCategory())
                    .append("aiGeneratedDescription", record.getAiGeneratedDescription())
                    .append("aiGeneratedTransactionParty", record.getAiGeneratedTransactionParty())
                    .append("userId", record.getUserId())
            );

            mongoOperations.getCollection(collectionName)
                    .updateOne(query, update, new com.mongodb.client.model.UpdateOptions().upsert(true));
        }
    }

    /**
     * Upserts a financial summary into MongoDB.
     * If summaryIdentifier exists, it updates the existing record; otherwise, it creates a new one.
     * 
     * @param summary The financial summary to upsert
     * @param collectionName The collection name to upsert into
     * @return The upserted financial summary
     */
    public FinancialSummary upsertFinancialSummary(FinancialSummary summary, String collectionName) {
        if (summary.getSummaryIdentifier() == null || summary.getSummaryIdentifier().isEmpty()) {
            // If no summaryIdentifier provided, just save as new document
            return mongoOperations.save(summary, collectionName);
        }

        // Create query to find existing document with the same summaryIdentifier and userId
        Document query = new Document()
                .append("summaryIdentifier", summary.getSummaryIdentifier())
                .append("userId", summary.getUserId());

        // Convert summary to Document for update operation
        Document summaryDoc = new Document();

        // Add all fields to update
        if (summary.getBankBalance() != null) summaryDoc.append("bankBalance", summary.getBankBalance());
        if (summary.getKiteWallet() != null) summaryDoc.append("kiteWallet", summary.getKiteWallet());
        summaryDoc.append("dcxWallet", summary.getDcxWallet());
        if (summary.getKitePosition() != null) summaryDoc.append("kitePosition", summary.getKitePosition());
        if (summary.getKiteHolding() != null) summaryDoc.append("kiteHolding", summary.getKiteHolding());
        if (summary.getDcxPosition() != null) summaryDoc.append("dcxPosition", summary.getDcxPosition());
        if (summary.getRupayCardBill() != null) summaryDoc.append("rupayCardBill", summary.getRupayCardBill());
        if (summary.getAmazonCardBill() != null) summaryDoc.append("amazonCardBill", summary.getAmazonCardBill());
        summaryDoc.append("expense", summary.getExpense());
        if (summary.getEarning() != null) summaryDoc.append("earning", summary.getEarning());
        summaryDoc.append("loss", summary.getLoss());
        if (summary.getLossEntries() != null) summaryDoc.append("lossEntries", summary.getLossEntries());
        if (summary.getTimestamp() != null) summaryDoc.append("timestamp", summary.getTimestamp());
        summaryDoc.append("userId", summary.getUserId());
        summaryDoc.append("summaryIdentifier", summary.getSummaryIdentifier());

        Document update = new Document("$set", summaryDoc);

        // Perform upsert operation
        mongoOperations.getCollection(collectionName)
                .updateOne(query, update, new com.mongodb.client.model.UpdateOptions().upsert(true));

        // Retrieve and return the updated document
        return mongoOperations.findOne(
                org.springframework.data.mongodb.core.query.Query.query(
                        org.springframework.data.mongodb.core.query.Criteria.where("summaryIdentifier").is(summary.getSummaryIdentifier())
                                .and("userId").is(summary.getUserId())
                ),
                FinancialSummary.class,
                collectionName
        );
    }
}
