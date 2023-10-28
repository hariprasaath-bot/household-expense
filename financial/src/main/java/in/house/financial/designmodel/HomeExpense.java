package in.house.financial.designmodel;

import in.house.financial.RequestDTO.ExpenseForm;
import in.house.financial.entity.HouseholdAccountsSplit;
import in.house.financial.entity.HouseholdAccountsTable;
import in.house.financial.entity.User;
import in.house.financial.interfaces.ExpenseService;
import in.house.financial.repository.HomeExpenseRepo;
import in.house.financial.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("home")
public class HomeExpense implements ExpenseService {

    @Autowired
    HomeExpenseRepo homeExpenseRepo;

    @Autowired
    UserRepository userRepo;
    @Override
    public void addExpense(ExpenseForm expenseForm) {
        HouseholdAccountsTable houseExpense = setHouseholdAccount(expenseForm);
        homeExpenseRepo.save(houseExpense);
    }

    @Override
    public void modifyExpense(ExpenseForm expenseForm) {

    }

    public HouseholdAccountsTable setHouseholdAccount(ExpenseForm expenseForm){
        HouseholdAccountsTable householdAccountsTable = new HouseholdAccountsTable();
        householdAccountsTable.setAcceptedStatus(expenseForm.getAcceptedStatus());
        householdAccountsTable.setTransactionType(expenseForm.getTransactionType());
        householdAccountsTable.setAmountValue(expenseForm.getAmountValue());
        List<HouseholdAccountsSplit> splits = expenseForm.getHouseholdAccountsSplit().stream()
                .map(split -> {
                    HouseholdAccountsSplit householdAccountsSplit = new HouseholdAccountsSplit();
                    Optional<User> user = userRepo.findById(split.getUserId());
                    householdAccountsSplit.setUser(user.get());
                    householdAccountsSplit.setTransactionType(split.getTransactionType());
                    householdAccountsSplit.setAmountValue(split.getAmountValue());
                    return householdAccountsSplit;
                })
                .collect(Collectors.toList());

        householdAccountsTable.setHouseholdAccountsSplits(splits);
        Optional<User> user = userRepo.findById(expenseForm.getUserId());
        householdAccountsTable.setUser(user.get());
        return  householdAccountsTable;
    }
}
