package in.house.financial.utils;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface StatementParsingUtil {

    List<TransactionRecord> parseBankStatement(MultipartFile statement);
}
