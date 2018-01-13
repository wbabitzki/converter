package ch.wba.accounting.converters;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class BigDecimalConverter {
    private static final NumberFormat FROM_STRING_FORMATTER = NumberFormat.getNumberInstance(new Locale("de", "CH"));
    private static final DecimalFormat TO_STRING_FORMATTER = new DecimalFormat("#0.00");

    public static BigDecimal toAmount(String amountAsString) {
        try {
            return new BigDecimal(FROM_STRING_FORMATTER.parse(amountAsString).toString()).setScale(2, RoundingMode.HALF_UP);
        } catch (ParseException e) {
            throw new  IllegalArgumentException("Can not convert to number '" + amountAsString + "'");
        }
    }

    public static String asString(BigDecimal number) {
        return TO_STRING_FORMATTER.format(number.setScale(2, RoundingMode.HALF_UP));
    }
}
