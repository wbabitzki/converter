package ch.wba.accounting.converters;

import ch.wba.accounting.format.FormatterWithDecimalSeparator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;

public class BigDecimalConverter {
    private static final int PCT_SCALE = 1;
    private static final int AMOUNT_SCALE = 2;
    private static final NumberFormat NUMBER_FORMATTER = FormatterWithDecimalSeparator.create();

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
        String onlyNumbers = amountAsString.replaceAll("[^0-9.-]", "");
        try {
            return new BigDecimal(NUMBER_FORMATTER.parse(onlyNumbers).toString());
        } catch (final ParseException e) {
            throw new IllegalArgumentException("Can not convert to number '" + amountAsString + "'");
        }
    }

    public static String asString(final BigDecimal number) {
        return NUMBER_FORMATTER.format(number.setScale(AMOUNT_SCALE, RoundingMode.HALF_UP));
    }
}
