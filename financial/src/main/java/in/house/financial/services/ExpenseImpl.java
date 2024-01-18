package in.house.financial.services;


import in.house.financial.RequestDTO.ExpenseForm;
import in.house.financial.interfaces.ExpenseService;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@Data
public class ExpenseImpl{


    private final ApplicationContext context;

    public void addExpense(ExpenseForm expense) {
        ExpenseService expenseService = (ExpenseService) getActualCLass(expense.getType());
        expenseService.addExpense(expense);
    }

    public void modifyExpense(ExpenseForm expense) {
        ExpenseService expenseService = (ExpenseService) getActualCLass(expense.getType());
        expenseService.modifyExpense(expense);
    }

    public Object getActualCLass(String type){
        return context.getBean(type);
    }
}
