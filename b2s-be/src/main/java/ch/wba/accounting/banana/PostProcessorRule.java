package ch.wba.accounting.banana;

import java.util.List;

abstract class PostProcessorRule {
    abstract boolean applicable(final BananaTransactionDto bananaTransactionDto);
    abstract List<BananaTransactionDto> apply(final int i, final List<BananaTransactionDto> transactions);

    List<BananaTransactionDto> conditionalApply(final int i, final List<BananaTransactionDto> transactions) {
        if (applicable(transactions.get(i))) {
            return apply(i, transactions);
        }
        return transactions;
    }
}
