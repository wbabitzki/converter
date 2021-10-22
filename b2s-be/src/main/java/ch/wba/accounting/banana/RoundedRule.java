package ch.wba.accounting.banana;

import java.util.List;

public class RoundedRule extends PostProcessorRule {
    @Override
    public boolean applicable(final BananaTransactionDto dto) {
        return BananaTransactionPredicates.isRounded.test(dto);
    }

    @Override
    public List<BananaTransactionDto> apply(final int i, final List<BananaTransactionDto> transactions) {
        final BananaTransactionDto roundedTransaction = transactions.get(i);
        final BananaTransactionDto mainTransaction = transactions.get(i - 1);
        if (!mainTransaction.getDocument().equals(roundedTransaction.getDocument())) {
            throw new IllegalArgumentException(String.format("The main transaction %s must belong to the same document as the rounded %s",
                    mainTransaction.getDocument(), roundedTransaction.getDocument()) );
        }
        mainTransaction.setAmountVat(mainTransaction.getAmountVat().add(roundedTransaction.getAmountVat()));
        mainTransaction.setAmountWithoutVat(mainTransaction.getAmountWithoutVat().subtract(roundedTransaction.getAmountVat()));
        return transactions;
    }
}
