package in.house.financial.interfaces;

import in.house.financial.RequestDTO.ExpenseForm;

public interface ExpenseService {

    public void addExpense(ExpenseForm expenseForm);
    public void modifyExpense(ExpenseForm expenseForm);
}
