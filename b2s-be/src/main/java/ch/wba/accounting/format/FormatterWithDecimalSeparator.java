package ch.wba.accounting.format;

import java.text.DecimalFormat;

public class FormatterWithDecimalSeparator extends DecimalFormat {

    private FormatterWithDecimalSeparator() {
        this.setDecimalSeparatorAlwaysShown(true);
        this.setGroupingUsed(false);
        this.setMinimumFractionDigits(2);
    }

    public static FormatterWithDecimalSeparator create() {
        return new FormatterWithDecimalSeparator();
    }
}