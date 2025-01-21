package in.house.financial.services;

import com.spire.pdf.PdfDocument;
import in.house.financial.configs.MongoConnector;
import in.house.financial.interfaces.BankStatementService;
import in.house.financial.utils.StatementParsingUtil;
import in.house.financial.utils.StatementRecordEnrich;
import in.house.financial.utils.TransactionRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import technology.tabula.*;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

import com.spire.pdf.utilities.PdfTable;
import com.spire.pdf.utilities.PdfTableExtractor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
@RequiredArgsConstructor
public class BankStatementServiceImpl implements BankStatementService {

     private final ApplicationContext context;

     private  final StatementRecordEnrich statementRecordEnrich;

     private final MongoConnector mongoConnector;

       @Override
     public String readStatementFile(String path) throws IOException {
           test(path);
            File pdfFile = new File(path);
            if (!pdfFile.exists()) {
                throw new IllegalArgumentException("PDF file not found at path: " + path);
            }
           PDDocument pdf = PDDocument.load(pdfFile,"hari0904");
            // Use a try-with-resources block for proper resource handling
           ObjectExtractor oe = new ObjectExtractor(pdf);
           SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
               PageIterator pages = oe.extract();
           while(pages.hasNext()) {
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
                           log.info("| {} |",cells.get(j).getText());
                           if (read == 1) {
                               System.out.print(cells.get(j).getText() + "|");
                           }
                       }
                   }
               }
           }
           return "guess success";

       }

       public void test(String path) throws IOException {

           //Load a sample PDF document
           PdfDocument pdf = new PdfDocument(path,"hari0904");

           //Create a StringBuilder instance
           StringBuilder builder = new StringBuilder();
           //Create a PdfTableExtractor instance
           PdfTableExtractor extractor = new PdfTableExtractor(pdf);

           //Loop through the pages in the PDF
           for (int pageIndex = 0; pageIndex < pdf.getPages().getCount(); pageIndex++) {
               //Extract tables from the current page into a PdfTable array
               PdfTable[] tableLists = extractor.extractTable(pageIndex);

               //If any tables are found
               if (tableLists != null && tableLists.length > 0) {
                   //Loop through the tables in the array
                   for (PdfTable table : tableLists) {
                       //Loop through the rows in the current table
                       for (int i = 0; i < table.getRowCount(); i++) {
                           //Loop through the columns in the current table
                           for (int j = 0; j < table.getColumnCount(); j++) {
                               //Extract data from the current table cell and append to the StringBuilder
                               String text = table.getText(i, j);
                                   builder.append(text + "|");
                           }
                           builder.append("+");
                       }
                   }
               }
           }

           // Parsing logic for CUB BANK Transaction statement from PDF Hard coded.
           String parsedData = builder.toString();

           String[] rows = parsedData.split("\\+");

           List<String> requiredFields = new ArrayList<>();
           List<Map<String,String>> data  = new ArrayList<>();
           AtomicReference<Boolean>  start = new AtomicReference<>(false);
           AtomicReference<Boolean> end = new AtomicReference<>(false);
           Arrays.stream(rows).forEach(r-> {
               String[] fields = r.split("\\|");
               int Totalcount = 5;
               int startCount = 0;
               Map<String, String> transactionData = new LinkedHashMap<>();
               if (!r.contains("BALANCE BROUGHT FORWARD")) {
                   for (String f : fields) {
                        if(fields.length == 9 || f.contains("Transaction Date") || f.contains("Total")) {
                            if (f.contains("Transaction Date")) {
                                start.set(true);
                                end.set(false);
                            }
                            if (f.contains("Total")) {
                                start.set(false);
                                end.set(true);
                            }
                            if (start.get() && !end.get()) {
                                f = f.strip();
                                f = f.replaceAll("\r\n", "");
                                if (startCount == 0) {
                                    transactionData.put("Transaction Data", f);
                                } else if (startCount == 2) {
                                    transactionData.put("details", f);
                                } else if (startCount == 4) {
                                    transactionData.put("debit", f);
                                } else if (startCount == 5) {
                                    transactionData.put("credit", f);
                                } else if (startCount == 8) {
                                    transactionData.put("balance", f);
                                }

                                startCount += 1;
                                if (!f.isEmpty()) {
                                    requiredFields.add(f);
                                }
                            }
                        }
                   }
                   if(!transactionData.isEmpty()) {
                       data.add(transactionData);
                   }
               }
           });

           //Write data into a .txt document
           FileWriter fw = new FileWriter("ExtractTable.txt");
           for (Map<String, String> rowData : data) {
               StringBuilder h = new StringBuilder();
               for (Map.Entry<String, String> entry : rowData.entrySet()) {
                   h.append(entry.getKey())
                           .append("::::[")
                           .append(entry.getValue())
                           .append("]::::"); // Or any desired delimiter
               }
               h.append("\n");
               h.deleteCharAt(h.length() - 1); // Remove trailing comma
               fw.write(h.toString() + "\n"); // Add newline after each row
           }
           fw.flush();
           fw.close();
       }

     public Object parseUploadedStatementFile(String bank, MultipartFile uploadedStatementFile) throws IOException{
           StatementParsingUtil statementParsingUtil = getStatementParser(bank);
           List<TransactionRecord> parsedStatement = statementParsingUtil.parseBankStatement(uploadedStatementFile);
           return  mongoConnector.putDataInMongo(statementRecordEnrich.enrich(parsedStatement), "transaction_records" );
     }

     /// Transactions --> MONGO
     /// FILTER BY TIME
     /// Group BY Category and TIME
     ///

     private StatementParsingUtil getStatementParser(String bank){
           return (StatementParsingUtil) context.getBean(bank);
     }



}

