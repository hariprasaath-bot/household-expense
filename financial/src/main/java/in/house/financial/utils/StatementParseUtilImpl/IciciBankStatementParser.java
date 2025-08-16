package in.house.financial.utils.StatementParseUtilImpl;

import in.house.financial.utils.DateConverterUtil;
import in.house.financial.utils.StatementParsingUtil;
import in.house.financial.utils.TransactionRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
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
                        && cell.getStringCellValue().trim().contains(headerName)) {
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
        if (cell.getCellType().equals(CellType.NUMERIC)) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return String.valueOf(cell.getDateCellValue());
            }
            return String.valueOf(cell.getNumericCellValue());
        }
        return cell.getStringCellValue();

    }

    /**
     * Parses the ICICI bank statement from the provided Excel file.
     *
     * @param statement The MultipartFile representing the bank statement.
     * @return A list of TransactionRecord objects representing the transactions in the statement.
     */

    @Override
    public List<TransactionRecord> parseBankStatement(MultipartFile statement) {
        List<TransactionRecord> transactions = new ArrayList<>();

        try (InputStream inputStream = statement.getInputStream();
             Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            int headerRowIndex = findHeaderRowIndex(sheet, "Transactions List -");
            int lastIndex = findHeaderRowIndex(sheet, "Legends Used in Account Statement");

            for (int i = headerRowIndex + 2; i < lastIndex; i++) {
                Row row = sheet.getRow(i);
                if (row == null || row.getCell(0) == null) {
                    continue;
                }
                try {
                    TransactionRecord transaction = new TransactionRecord();
                    try{
                        transaction.setRecordDate(DateConverterUtil.convertToDate(getCellValue(row.getCell(2)))); // Column B
                    } catch (Exception e) {
                        log.error("Error parsing record date for row {}: :{} ::: {}", i, row, e.getMessage());
                        transaction.setRecordDate(transactions.get(transactions.size() - 1 ).getRecordDate()); // Fallback to previous record date
                    }
                    try{
                        transaction.setTransaction_date(DateConverterUtil.convertToDate(getCellValue(row.getCell(3)))); // Column C
                    } catch (Exception e) {
                        log.error("Error parsing transaction date for row {}: :{} ::: {}", i, row, e.getMessage());
                        transaction.setTransaction_date(transactions.get(transactions.size() - 1 ).getTransaction_date()); // Fallback to record date
                    }transaction.setRemark(getCellValue(row.getCell(5)));// Column D
                    transaction.setTransactionId(StringUtils.substringAfterLast(getCellValue(row.getCell(5)), "/")); // Column D
                    transaction.setWithdraw(Double.valueOf(getCellValue(row.getCell(6)))); // Column E
                    transaction.setDeposit(Double.valueOf(getCellValue(row.getCell(7)))); // Column F
                    transaction.setBalance(getCellValue(row.getCell(8))); // Column G

                    transactions.add(transaction);
                } catch (Exception invalidRecordEx) {
                    log.info("Invalid Record found {}", row.getRowNum());
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
