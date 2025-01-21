package in.house.financial.utils;

import java.util.List;
import java.util.Map;

public interface StatementRecordEnrich {

    List<TransactionRecord> enrich(final List<TransactionRecord> statements);

}
