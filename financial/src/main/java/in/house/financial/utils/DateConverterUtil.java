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





    private static final SimpleDateFormat customDateFormat = new SimpleDateFormat("dd,MM,yyyy");

    public static Date convertToDate(String dateString) {
        try {
            return customDateFormat.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format: " + dateString, e);
        }
    }
}
