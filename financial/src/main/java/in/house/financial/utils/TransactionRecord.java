package in.house.financial.utils;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRecord {
    private Object recordDate;
    private Object transaction_date ;
    private String remark ;
    private Double withdraw ;
    private Double deposit ;
    private Object balance ;
    private String transactionType;
    private String category;
    private Boolean isUpiTransaction ;
}
