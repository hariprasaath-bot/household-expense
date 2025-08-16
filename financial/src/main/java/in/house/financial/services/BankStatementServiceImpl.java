package in.house.financial.services;

import in.house.financial.RequestDTO.UploadProgress;
import in.house.financial.configs.MongoConnector;
import in.house.financial.dto.CategoryRequest;
import in.house.financial.entity.Categories;
import in.house.financial.interfaces.BankStatementService;
import in.house.financial.repository.CategoriesRepo;
import in.house.financial.securityconfig.UserSession;
import in.house.financial.utils.ServerSentEventUtil;
import in.house.financial.utils.StatementParsingUtil;
import in.house.financial.utils.StatementRecordEnrich;
import in.house.financial.utils.TransactionRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.context.ApplicationContext;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import technology.tabula.*;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

//import com.spire.pdf.utilities.PdfTable;
//import com.spire.pdf.utilities.PdfTableExtractor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import in.house.financial.model.FinancialSummary;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.domain.Sort;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
@RequiredArgsConstructor
public class BankStatementServiceImpl implements BankStatementService {

    private final ApplicationContext context;

    private final StatementRecordEnrich statementRecordEnrich;

    private final MongoConnector mongoConnector;

    private final MongoOperations mongoOperations;

    private final ServerSentEventUtil severSentEventUtil;

    private final UserSession userSession;

    private final CategoriesRepo categoriesRepo;

    @Override
    public String readStatementFile(String path) throws IOException {
//        test(path);
        File pdfFile = new File(path);
        if (!pdfFile.exists()) {
            throw new IllegalArgumentException("PDF file not found at path: " + path);
        }
        PDDocument pdf = PDDocument.load(pdfFile, "hari0904");
        // Use a try-with-resources block for proper resource handling
        ObjectExtractor oe = new ObjectExtractor(pdf);
        SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
        PageIterator pages = oe.extract();
        while (pages.hasNext()) {
            // extract text from the table after detecting
            List<Table> table = sea.extract(pages.next());
            for (Table tables : table) {
                List<List<RectangularTextContainer>> rows = tables.getRows();

                for (int i = 0; i < rows.size(); i++) {

                    List<RectangularTextContainer> cells = rows.get(i);
                    int read = 0;
                    for (int j = 0; j < cells.size(); j++) {
                        if (cells.get(j).getText().contains("Statement of Transactions in Savings")) {
                            read = 1;
                        }
                        log.info("| {} |", cells.get(j).getText());
                        if (read == 1) {
                            System.out.print(cells.get(j).getText() + "|");
                        }
                    }
                }
            }
        }
        return "guess success";

    }

//    public void test(String path) throws IOException {
//
//        //Load a sample PDF document
//        PdfDocument pdf = new PdfDocument(path, "hari0904");
//
//        //Create a StringBuilder instance
//        StringBuilder builder = new StringBuilder();
//        //Create a PdfTableExtractor instance
//        PdfTableExtractor extractor = new PdfTableExtractor(pdf);
//
//        //Loop through the pages in the PDF
//        for (int pageIndex = 0; pageIndex < pdf.getPages().getCount(); pageIndex++) {
//            //Extract tables from the current page into a PdfTable array
//            PdfTable[] tableLists = extractor.extractTable(pageIndex);
//
//            //If any tables are found
//            if (tableLists != null && tableLists.length > 0) {
//                //Loop through the tables in the array
//                for (PdfTable table : tableLists) {
//                    //Loop through the rows in the current table
//                    for (int i = 0; i < table.getRowCount(); i++) {
//                        //Loop through the columns in the current table
//                        for (int j = 0; j < table.getColumnCount(); j++) {
//                            //Extract data from the current table cell and append to the StringBuilder
//                            String text = table.getText(i, j);
//                            builder.append(text + "|");
//                        }
//                        builder.append("+");
//                    }
//                }
//            }
//        }
//
//        // Parsing logic for CUB BANK Transaction statement from PDF Hard coded.
//        String parsedData = builder.toString();
//
//        String[] rows = parsedData.split("\\+");
//
//        List<String> requiredFields = new ArrayList<>();
//        List<Map<String, String>> data = new ArrayList<>();
//        AtomicReference<Boolean> start = new AtomicReference<>(false);
//        AtomicReference<Boolean> end = new AtomicReference<>(false);
//        Arrays.stream(rows).forEach(r -> {
//            String[] fields = r.split("\\|");
//            int Totalcount = 5;
//            int startCount = 0;
//            Map<String, String> transactionData = new LinkedHashMap<>();
//            if (!r.contains("BALANCE BROUGHT FORWARD")) {
//                for (String f : fields) {
//                    if (fields.length == 9 || f.contains("Transaction Date") || f.contains("Total")) {
//                        if (f.contains("Transaction Date")) {
//                            start.set(true);
//                            end.set(false);
//                        }
//                        if (f.contains("Total")) {
//                            start.set(false);
//                            end.set(true);
//                        }
//                        if (start.get() && !end.get()) {
//                            f = f.strip();
//                            f = f.replaceAll("\r\n", "");
//                            if (startCount == 0) {
//                                transactionData.put("Transaction Data", f);
//                            } else if (startCount == 2) {
//                                transactionData.put("details", f);
//                            } else if (startCount == 4) {
//                                transactionData.put("debit", f);
//                            } else if (startCount == 5) {
//                                transactionData.put("credit", f);
//                            } else if (startCount == 8) {
//                                transactionData.put("balance", f);
//                            }
//
//                            startCount += 1;
//                            if (!f.isEmpty()) {
//                                requiredFields.add(f);
//                            }
//                        }
//                    }
//                }
//                if (!transactionData.isEmpty()) {
//                    data.add(transactionData);
//                }
//            }
//        });
//
//        //Write data into a .txt document
//        FileWriter fw = new FileWriter("ExtractTable.txt");
//        for (Map<String, String> rowData : data) {
//            StringBuilder h = new StringBuilder();
//            for (Map.Entry<String, String> entry : rowData.entrySet()) {
//                h.append(entry.getKey())
//                        .append("::::[")
//                        .append(entry.getValue())
//                        .append("]::::"); // Or any desired delimiter
//            }
//            h.append("\n");
//            h.deleteCharAt(h.length() - 1); // Remove trailing comma
//            fw.write(h.toString() + "\n"); // Add newline after each row
//        }
//        fw.flush();
//        fw.close();
//    }

    public void processStatementFile(String bank, MultipartFile uploadedStatementFile, long uniqueEmitterId) {
        severSentEventUtil.progressEmitters.put(uniqueEmitterId, new SseEmitter(-1L));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        String loggedInUserId = userSession.getUser().getId();
        log.info("Logged in user id :: {}", loggedInUserId);
        executor.execute(() -> {
            severSentEventUtil.sendProgressUpdate("Started processing the uploaded statement file", 5, uniqueEmitterId);
            StatementParsingUtil statementParsingUtil = getStatementParser(bank);
            List<TransactionRecord> parsedStatement = statementParsingUtil.parseBankStatement(uploadedStatementFile);

            severSentEventUtil.sendProgressUpdate("Completed parsing the uploaded statement file -- no of records: " + parsedStatement.size(), 30, uniqueEmitterId);
            List<TransactionRecord> enrichedRecords = statementRecordEnrich.enrich(parsedStatement, uniqueEmitterId);

            severSentEventUtil.sendProgressUpdate("Completed enriching the parsed statement file -- no of records: " + enrichedRecords.size(), 80, uniqueEmitterId);

            severSentEventUtil.sendProgressUpdate("storing the enriched statement file in MongoDB", 90, uniqueEmitterId);
            enrichedRecords.forEach(e->e.setUserId(loggedInUserId));
            mongoConnector.upsertRecords(enrichedRecords,"bank_transactions");
            log.info("Added records :: {}",enrichedRecords.size());

            severSentEventUtil.sendProgressUpdate("Process completed", 100, uniqueEmitterId);
            severSentEventUtil.progressEmitters.get(uniqueEmitterId).complete();
        });
    }




    /// Transactions --> MONGO
    /// FILTER BY TIME
    /// Group BY Category and TIME

    private StatementParsingUtil getStatementParser(String bank) {
        return (StatementParsingUtil) context.getBean(bank);
    }

    @Override
    public List<Categories>  getBankCategories() {
        String userId = userSession.getUser().getId();
        List<Categories> categories = categoriesRepo.findByUserIdOrUserIdIsNull(userId);
        return categories;
    }


    @Override
    public FinancialSummary saveFinancialSummary(FinancialSummary financialSummary) {
        // Set the user ID from the current logged-in user if not already set
        if (financialSummary.getUserId() == null || financialSummary.getUserId().isEmpty()) {
            String loggedInUserId = userSession.getUser().getId();
            financialSummary.setUserId(loggedInUserId);
        }

        // Set timestamp if not already set
        if (financialSummary.getTimestamp() == null) {
            financialSummary.setTimestamp(LocalDateTime.now());
        }

        // Save to MongoDB collection
        return mongoOperations.save(financialSummary, "financial_summary");
    }

    @Override
    public FinancialSummary saveOrUpdateFinancialSummary(FinancialSummary financialSummary) {
        // Set the user ID from the current logged-in user if not already set
        if (financialSummary.getUserId() == null || financialSummary.getUserId().isEmpty()) {
            String loggedInUserId = userSession.getUser().getId();
            financialSummary.setUserId(loggedInUserId);
        }

        // Set timestamp if not already set
        if (financialSummary.getTimestamp() == null) {
            financialSummary.setTimestamp(LocalDateTime.now());
        }

        // Generate summaryIdentifier if not provided
        if (financialSummary.getSummaryIdentifier() == null || financialSummary.getSummaryIdentifier().isEmpty()) {
            // Generate a unique identifier based on timestamp and user ID
            financialSummary.setSummaryIdentifier(financialSummary.getUserId() + "-" + System.currentTimeMillis());
        }

        // Use the mongoConnector to upsert the document
        return mongoConnector.upsertFinancialSummary(financialSummary, "financial_summary");
    }

    @Override
    public FinancialSummary getFinancialSummaryByIdentifier(String summaryIdentifier, String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(summaryIdentifier)
                .and("userId").is(userId));

        return mongoOperations.findOne(query, FinancialSummary.class, "financial_summary");
    }

    @Override
    public List<FinancialSummary> getFinancialSummaryByUserIdAndTimeRange(String userId, LocalDateTime startTime, LocalDateTime endTime) {
        Query query = new Query();

        // Filter by userId
        query.addCriteria(Criteria.where("userId").is(userId));

        // Add time range filter if provided
        if (startTime != null && endTime != null) {
            query.addCriteria(Criteria.where("timestamp").gte(startTime).lte(endTime));
        } else if (startTime != null) {
            query.addCriteria(Criteria.where("timestamp").gte(startTime));
        } else if (endTime != null) {
            query.addCriteria(Criteria.where("timestamp").lte(endTime));
        }

        // Sort by timestamp in descending order (newest first)
        query.with(Sort.by(Sort.Direction.DESC, "timestamp"));

        return mongoOperations.find(query, FinancialSummary.class, "financial_summary");
    }

    @Override
    public List<TransactionRecord> getBankTransactionsByUserIdAndDateRange(String userId, Date startDate, Date endDate) {
        Query query = new Query();

        // Filter by userId
        query.addCriteria(Criteria.where("userId").is(userId));

        // Add date range filter if provided
        if (startDate != null && endDate != null) {
            query.addCriteria(Criteria.where("transaction_date").gte(startDate).lte(endDate));
        } else if (startDate != null) {
            query.addCriteria(Criteria.where("transaction_date").gte(startDate));
        } else if (endDate != null) {
            query.addCriteria(Criteria.where("transaction_date").lte(endDate));
        }

        // Sort by transaction date in descending order (newest first)
        query.with(Sort.by(Sort.Direction.DESC, "transaction_date"));

        return mongoOperations.find(query, TransactionRecord.class, "bank_transactions");
    }

    @Override
    public int updateBankTransactions(List<TransactionRecord> transactionRecords) {
        if (transactionRecords == null || transactionRecords.isEmpty()) {
            return 0;
        }

        // Get the user ID from the current session
        String userId = userSession.getUser().getId();

        // Set the user ID for all records
        for (TransactionRecord record : transactionRecords) {
            // Ensure the transaction has an ID
            if (record.getTransactionId() == null || record.getTransactionId().isEmpty()) {
                log.error("Cannot update transaction record without transactionId");
                continue;
            }

            // Set the current user ID
            record.setUserId(userId);
        }

        // Bulk update all records at once
        mongoConnector.upsertRecords(transactionRecords, "bank_transactions");

        return transactionRecords.size();
    }
}
