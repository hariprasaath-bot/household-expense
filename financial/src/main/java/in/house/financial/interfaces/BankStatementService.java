package in.house.financial.interfaces;

import java.io.IOException;

public interface BankStatementService {

    public String readStatementFile(String path) throws IOException;
}
