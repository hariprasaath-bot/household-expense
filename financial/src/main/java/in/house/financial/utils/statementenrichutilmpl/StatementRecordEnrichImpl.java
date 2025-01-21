package in.house.financial.utils.statementenrichutilmpl;

import in.house.financial.constants.TransactionConstants;
import in.house.financial.utils.StatementRecordEnrich;
import in.house.financial.utils.TransactionRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StatementRecordEnrichImpl implements StatementRecordEnrich {

    @Override
    public List<TransactionRecord> enrich(List<TransactionRecord> statements) {

        statements.forEach(st-> {
            if(st.getDeposit().compareTo(0.0) == 0){
                st.setTransactionType("WITHDRAW");
            }else if(st.getWithdraw().compareTo(0.0) == 0){
                st.setTransactionType("DEPOSIT");
            }
            if (StringUtils.containsIgnoreCase(st.getRemark() ,"upi" )) {
                st.setIsUpiTransaction(true);
            } else {
                st.setIsUpiTransaction(false);
            }
            setCategoryByKnownRemarks(st);
        });
        return statements;
    }

    private void setCategoryByKnownRemarks(TransactionRecord statement){
        if(StringUtils.containsIgnoreCase(statement.getRemark() ,"zomato" )){
            statement.setCategory(TransactionConstants.FOOD);
        }
        if(StringUtils.containsIgnoreCase(statement.getRemark() ,"zerodha" )){
            if(statement.getTransactionType().equalsIgnoreCase("WITHDRAW")){
                statement.setCategory(TransactionConstants.SAVING);
            }
        }
        if(StringUtils.containsIgnoreCase(statement.getRemark() ,"smartq" )){
                statement.setCategory(TransactionConstants.FOOD);
        }
        if(StringUtils.containsIgnoreCase(statement.getRemark(),"ICICI BANK CREDIT CA")){
            statement.setCategory(TransactionConstants.BILL);
        }
    }
}
