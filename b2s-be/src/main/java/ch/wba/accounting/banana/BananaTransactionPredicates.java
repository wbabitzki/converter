package ch.wba.accounting.banana;

import java.util.function.Predicate;

public class BananaTransactionPredicates {
    protected static final String VAT_UP_ROUNDED_CODE = "M77-2";
    protected static final String VAT_OFF_ROUNDED_CODE = "-M77-2";
    protected static final String VAT_UST_77_CODE = "USt77";
    protected static final String VAT_REVERSAL = "-VSM77";

    private BananaTransactionPredicates() {
        // Empty
    }

    static final Predicate<BananaTransactionDto> isRounded = transactions -> {
        final String vatCode = transactions.getVatCode();
        return VAT_UP_ROUNDED_CODE.equals(vatCode) || VAT_OFF_ROUNDED_CODE.equals(vatCode);
    };

    static final Predicate<BananaTransactionDto> isUst = transactions -> {
        final String vatCode = transactions.getVatCode();
        return VAT_UST_77_CODE.equalsIgnoreCase(vatCode);
    };

    static final Predicate<BananaTransactionDto> isReversal = transactions -> {
        final String vatCode = transactions.getVatCode();
        return VAT_REVERSAL.equalsIgnoreCase(vatCode);
    };
}
