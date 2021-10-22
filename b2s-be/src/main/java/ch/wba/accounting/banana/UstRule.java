package ch.wba.accounting.banana;

import java.util.List;

import static ch.wba.accounting.banana.BananaTransactionPredicates.VAT_UST_77_CODE;

public class UstRule extends PostProcessorRule {
    @Override
    public boolean applicable(final BananaTransactionDto dto) {
        return BananaTransactionPredicates.isUst.test(dto);
    }

    @Override
    public List<BananaTransactionDto> apply(final int i, final List<BananaTransactionDto> transactions) {
        transactions.get(i).setVatCode(VAT_UST_77_CODE);
        return transactions;
    }
}
