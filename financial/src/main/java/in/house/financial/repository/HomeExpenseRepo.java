package in.house.financial.repository;

import in.house.financial.entity.HouseholdAccountsTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeExpenseRepo extends JpaRepository<HouseholdAccountsTable, Integer> {

}
