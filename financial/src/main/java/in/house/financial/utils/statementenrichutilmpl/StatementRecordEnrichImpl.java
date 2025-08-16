package in.house.financial.utils.statementenrichutilmpl;

import in.house.financial.constants.TransactionConstants;
import in.house.financial.entity.Categories;
import in.house.financial.repository.CategoriesRepo;
import in.house.financial.securityconfig.UserSession;
import in.house.financial.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class StatementRecordEnrichImpl implements StatementRecordEnrich {

    private final GeminiAiHelper aiHelper;

    private final ServerSentEventUtil serverSentEventUtil;

    private final CategoriesRepo categoriesRepo;

    private final UserSession userSession;

    @Override
    public List<TransactionRecord> enrich(List<TransactionRecord> statements, long uniqueEmitterId) {
        String userId = userSession.getUser().getId();
        List<Categories> categories = categoriesRepo.findByUserIdOrUserIdIsNull(userId);
        // Process records in batches of 25
        statements.forEach(st -> {
            if (st.getDeposit().compareTo(0.0) == 0) {
                st.setTransactionType("WITHDRAW");
            } else if (st.getWithdraw().compareTo(0.0) == 0) {
                st.setTransactionType("DEPOSIT");
            }
            if (StringUtils.containsIgnoreCase(st.getRemark(), "upi")) {
                st.setIsUpiTransaction(true);
            } else {
                st.setIsUpiTransaction(false);
            }
            setCategoryByKnownRemarks(st, categories);
        });
        serverSentEventUtil.sendProgressUpdate("static enrich data completed..", 40, uniqueEmitterId);
        int batchSize = 15;
        int count = 0;
        for (int i = 0; i < statements.size(); i += batchSize) {
            int end = Math.min(i + batchSize, statements.size());
            List<TransactionRecord> batch = statements.subList(i, end);

            List<String> remarks = batch.stream()
                    .map(TransactionRecord::getRemark)
                    .collect(Collectors.toList());

            List<List<String>> aiPredictions = aiHelper.predictBatchTransactionPartyAndCategory(remarks);
            log.info("AI prediction : {}, batch sent {}", aiPredictions.size(), remarks.size());
            try {
                for (int j = 0; j < remarks.size(); j++) {
                    TransactionRecord record = batch.get(j);
                    List<String> prediction = aiPredictions.get(j);
                    record.setAiGeneratedCategory(prediction.get(0));
                    record.setAiGeneratedTransactionParty(prediction.get(1));
                }
            } catch (Exception e) {
                log.error("Error processing batch: {}, error: {}", batch, e.getMessage());
            }
            try {
                Thread.sleep(50000); // Sleep for 30 seconds to respect 10 RPM
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                log.warn("Batch sleep interrupted", ie);
            }
            count += 2;
            serverSentEventUtil.sendProgressUpdate("AI enrich data in progress.." + "AI prediction : " +  aiPredictions.size() +" batch sent :" + remarks.size(), 50 + count, uniqueEmitterId);
        }
        serverSentEventUtil.sendProgressUpdate("AI Enrichment of data completed..", 60, uniqueEmitterId);
        return statements;
    }

    private void setCategoryByKnownRemarks(TransactionRecord statement, List<Categories> categories) {
        // Default category
        statement.setCategory(TransactionConstants.UNKNOWN);

        for (Categories category : categories) {
            if (category.getSearchString() != null && !category.getSearchString().isEmpty()) {
                String[] searchStrings = category.getSearchString().split(",");
                for (String s : searchStrings) {
                    if (StringUtils.containsIgnoreCase(statement.getRemark(), s.trim())) {
                        statement.setCategory(category.getName());
                        return; // Exit after finding the first matching category
                    }
                }
            }
        }
    }
}
