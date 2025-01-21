package in.house.financial.repository;

import in.house.financial.entity.HouseholdAccountsTable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HomeExpenseRepo extends MongoRepository<HouseholdAccountsTable, String> {

}
