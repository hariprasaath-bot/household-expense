package in.house.financial.utils.StatementParseUtilImpl;

import in.house.financial.utils.DateConverterUtil;
import in.house.financial.utils.StatementParsingUtil;
import in.house.financial.utils.TransactionRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component("icici")
@RequiredArgsConstructor
public class IciciBankStatementParser implements StatementParsingUtil {

    private int findHeaderRowIndex(Sheet sheet, String headerName) {
        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell.getCellType() == CellType.STRING
                        && headerName.contains(cell.getStringCellValue().trim())) {
                    return row.getRowNum(); // Return the row index if header is found
                }
            }
        }
        throw new IllegalArgumentException("Header '" + headerName + "' not found in the sheet");
    }


    // Utility method to extract cell values based on type
    private String getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        return cell.getStringCellValue();

    }

    @Override
    public List<TransactionRecord> parseBankStatement(MultipartFile statement) {
        List<TransactionRecord> transactions = new ArrayList<>();

        try (InputStream inputStream = statement.getInputStream();
             Workbook workbook =  WorkbookFactory.create(inputStream)) {

            // Assuming the first sheet contains the data
            Sheet sheet = workbook.getSheetAt(0);

            // Find the header row index dynamically
            int headerRowIndex = findHeaderRowIndex(sheet, "Transactions List -");
            int lastIndex = findHeaderRowIndex(sheet,"Legends Used in Account Statement");

            // Process rows after the header
            for (int i = headerRowIndex + 1 + 1; i < lastIndex; i++) {
                Row row = sheet.getRow(i);

                // Skip empty rows
                if (row == null || row.getCell(0) == null) {
                    continue;
                }
                try {
                    // Read transaction details
                    TransactionRecord transaction = new TransactionRecord();
                    transaction.setRecordDate(DateConverterUtil.convertToEpoch(getCellValue(row.getCell(2)))); // Column B
                    transaction.setTransaction_date(DateConverterUtil.convertToEpoch(getCellValue(row.getCell(3)))); // Column C
                    transaction.setRemark((String) getCellValue(row.getCell(5))); // Column D
                    transaction.setWithdraw(Double.valueOf((String) getCellValue(row.getCell(6)))); // Column E
                    transaction.setDeposit(Double.valueOf((String) getCellValue(row.getCell(7)))); // Column F
                    transaction.setBalance(getCellValue(row.getCell(8))); // Column G

                    transactions.add(transaction);
                }catch (Exception invalidRecordEx){
                    log.info("Invalid Record found {}",row.getRowNum());
                    log.error(invalidRecordEx.getMessage());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error parsing the bank statement: " + e.getMessage());
        }

        return transactions;
    }

}
