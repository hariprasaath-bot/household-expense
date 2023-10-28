package in.house.financial.designmodel;

import in.house.financial.RequestDTO.ExpenseForm;
import in.house.financial.interfaces.ExpenseService;
import org.springframework.stereotype.Component;

@Component("personal")
public class PersonalExpense implements ExpenseService {

    @Override
    public void addExpense(ExpenseForm expenseForm) {

    }

    @Override
    public void modifyExpense(ExpenseForm expenseForm) {

    }
}

