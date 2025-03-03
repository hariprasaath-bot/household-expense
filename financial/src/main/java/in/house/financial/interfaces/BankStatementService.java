package in.house.financial.interfaces;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

public interface BankStatementService {

    String readStatementFile(String path) throws IOException;

    SseEmitter parseUploadedStatementFile(String bank, MultipartFile uploadedStatementFile, SseEmitter emitter) throws IOException;
}
