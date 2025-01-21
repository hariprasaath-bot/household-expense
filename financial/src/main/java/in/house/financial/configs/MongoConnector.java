package in.house.financial.configs;

import in.house.financial.utils.TransactionRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class MongoConnector {

    private final MongoOperations mongoOperations;

    public Object putDataInMongo(List<TransactionRecord> data, String collectionName) {
        return mongoOperations.insert(data, collectionName);
    }
}
