package in.house.financial.interfaces;

import in.house.financial.RequestDTO.ExpenseForm;

public interface ExpenseService {

    void addExpense(ExpenseForm expenseForm);

    void modifyExpense(ExpenseForm expenseForm);
}
