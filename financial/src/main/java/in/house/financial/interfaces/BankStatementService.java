package in.house.financial.interfaces;

import java.io.IOException;

public interface BankStatementService {

    String readStatementFile(String path) throws IOException;
}
