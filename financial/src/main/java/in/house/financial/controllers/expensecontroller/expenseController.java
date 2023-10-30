package in.house.financial.controllers.expensecontroller;

import in.house.financial.RequestDTO.ExpenseForm;
import in.house.financial.services.ExpenseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/expense")
public class expenseController {

    @Autowired
    ExpenseImpl expenseImpl;
    @PostMapping("/add")
    public void addExpense(@RequestBody ExpenseForm expense){
        expenseImpl.addExpense(expense);
    }
}
