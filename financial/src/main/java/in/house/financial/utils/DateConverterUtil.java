package in.house.financial.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@Component
@Slf4j
public class DateConverterUtil {

    public static long convertToEpoch(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd,MM,yyyy");
        Date requiredDate = formatter.parse(date);
        return requiredDate.getTime();
    }
}
