package ch.wba.accounting.banana;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import static ch.wba.accounting.banana.BananacConstants.VAT_UST_PREFIX;

public class BananaTransactionPredicates {
    private static final String VAT_REVERSAL = "-VSM77";
    private static final Pattern ROUNDED_CODE_PATTERN = Pattern.compile("^-?M\\d{2}-\\d$");

    private BananaTransactionPredicates() {
        // Empty
    }

    static final Predicate<BananaTransactionDto> isRounded = transactions -> {
        final String vatCode = transactions.getVatCode();
        return ROUNDED_CODE_PATTERN.matcher(vatCode).matches();
    };

    static final Predicate<BananaTransactionDto> isUst = transactions -> {
        final String vatCode = transactions.getVatCode();
        return vatCode != null && vatCode.toLowerCase().startsWith(VAT_UST_PREFIX.toLowerCase());
    };

    static final Predicate<BananaTransactionDto> isReversal = transactions -> {
        final String vatCode = transactions.getVatCode();
        return VAT_REVERSAL.equalsIgnoreCase(vatCode);
    };
}
