package in.house.financial.RequestDTO;

import lombok.Data;

@Data
public class HouseholdAccountsSplitDTO {

    private Long id;
    private Long userId;
    private String transactionType;
    private Double amountValue;

}
