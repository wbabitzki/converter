package ch.wba.accounting.converters;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class BigDecimalConverter {
    private static final int PCT_SCALE = 1;
    private static final int AMOUNT_SCALE = 2;
    private static final NumberFormat NUMBER_FORMATTER = createNumberFormatter();

    private static NumberFormat createNumberFormatter() {
        NumberFormat format = NumberFormat.getNumberInstance(new Locale("de", "CH"));
        format.setMinimumFractionDigits(2);
        return format;
    }

    private BigDecimalConverter() {
        // Empty
    }

    public static BigDecimal toAmount(final String amountAsString) {
        return toBigDecimal(amountAsString).setScale(AMOUNT_SCALE, RoundingMode.HALF_UP);
    }

    public static BigDecimal toPct(final String amountAsString) {
        return toBigDecimal(amountAsString).setScale(PCT_SCALE, RoundingMode.HALF_UP);
    }

    private static BigDecimal toBigDecimal(final String amountAsString) {
        try {
            return new BigDecimal(NUMBER_FORMATTER.parse(amountAsString).toString());
        } catch (final ParseException e) {
            throw new IllegalArgumentException("Can not convert to number '" + amountAsString + "'");
        }
    }

    public static String asString(final BigDecimal number) {
        return NUMBER_FORMATTER.format(number.setScale(2, RoundingMode.HALF_UP));
    }
}
