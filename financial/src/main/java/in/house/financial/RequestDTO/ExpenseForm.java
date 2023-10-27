package in.house.financial.RequestDTO;

import lombok.Data;
import java.util.List;

@Data
public class ExpenseForm {

    private Long id;
    private Long userId;
    private String transactionType;
    private Double amountValue;
    private String acceptedStatus;
    private List<HouseholdAccountsSplitDTO> householdAccountsSplit;
    private String type;

}

