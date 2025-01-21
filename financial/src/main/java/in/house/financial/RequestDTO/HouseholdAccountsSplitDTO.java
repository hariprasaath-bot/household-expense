package in.house.financial.RequestDTO;

import lombok.Data;

@Data
public class HouseholdAccountsSplitDTO {
    private String userId;
    private String transactionType;
    private Double amountValue;

}
