package ch.wba.accounting.banana;

import java.util.List;

import static ch.wba.accounting.banana.BananacConstants.VAT_UST_PREFIX;

public class UstRule extends PostProcessorRule {
    @Override
    public boolean applicable(final BananaTransactionDto dto) {
        return BananaTransactionPredicates.isUst.test(dto);
    }

    @Override
    public List<BananaTransactionDto> apply(final int i, final List<BananaTransactionDto> transactions) {
        final String vatCode = transactions.get(i).getVatCode();
        transactions.get(i).setVatCode(VAT_UST_PREFIX.concat(vatCode.substring(VAT_UST_PREFIX.length())));
        return transactions;
    }
}
