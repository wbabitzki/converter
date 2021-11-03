package ch.wba.accounting.banana;

import java.util.List;

public class ReversalRule extends PostProcessorRule {
    @Override
    boolean applicable(BananaTransactionDto dto) {
        return BananaTransactionPredicates.isReversal.test(dto);
    }

    @Override
    List<BananaTransactionDto> apply(int i, List<BananaTransactionDto> transactions) {
        final BananaTransactionDto banana = transactions.get(i);
        banana.setReversal(true);
        banana.setVatCode(BananacConstants.VAT_VSM_77_CODE);
        banana.setAmount(banana.getAmount().negate());
        return transactions;
    }
}
