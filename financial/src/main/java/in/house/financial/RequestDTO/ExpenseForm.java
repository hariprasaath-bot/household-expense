package in.house.financial.RequestDTO;

import lombok.Data;

import java.util.List;

@Data
public class ExpenseForm {
    private String userId;
    private String transactionType;
    private Double amountValue;
    private String acceptedStatus;
    private List<HouseholdAccountsSplitDTO> householdAccountsSplit;
    private String type;

}

