package converters;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class BigDecimalConverter {
    private static final NumberFormat AMOUNT_FORMATTER = NumberFormat.getNumberInstance(new Locale("de", "CH"));

    public static BigDecimal toAmount(String amountAsString) {
        try {
            return new BigDecimal(AMOUNT_FORMATTER.parse(amountAsString).doubleValue());
        } catch (ParseException e) {
            throw new  IllegalArgumentException("Can not convert to number '" + amountAsString + "'");
        }
    }
}
