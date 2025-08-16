package in.house.financial.utils;

import java.util.List;

public interface StatementRecordEnrich {

    List<TransactionRecord> enrich(final List<TransactionRecord> statements, long uniqueEmitterId);

}
