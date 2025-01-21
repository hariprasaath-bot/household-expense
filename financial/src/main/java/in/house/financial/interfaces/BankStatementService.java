package in.house.financial.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface BankStatementService {

    String readStatementFile(String path) throws IOException;

    Object parseUploadedStatementFile(String bank, MultipartFile uploadedStatementFile) throws IOException;
}
