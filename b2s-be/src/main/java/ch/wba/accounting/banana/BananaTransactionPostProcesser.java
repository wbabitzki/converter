package ch.wba.accounting.banana;

import static ch.wba.accounting.banana.BananaTransactionPredicates.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BananaTransactionPostProcesser {
    abstract class TransactionAdjuster implements Consumer<Integer> {
        List<BananaTransactionDto> transactions = null;

        TransactionAdjuster(final List<BananaTransactionDto> transactions) {
            this.transactions = transactions;
        }
    }

    public List<BananaTransactionDto> adjustTransactions(final List<BananaTransactionDto> transactions) {
        final Map<Predicate<BananaTransactionDto>, Consumer<Integer>> adjustRules = new HashMap<>();
        adjustRules.put(isRounded, createRoundedAdjuster(transactions));
        adjustRules.put(isUst, createUstAdjuster(transactions));
        applyRules(adjustRules, transactions);
        return transactions.stream() //
            .filter(isRounded.negate()) //
            .collect(Collectors.toList());
    }

    private void applyRules(final Map<Predicate<BananaTransactionDto>, Consumer<Integer>> adjustRules, final List<BananaTransactionDto> transactions) {
        for (int i = 0; i < transactions.size(); i++) {
            final int n = i;
            adjustRules.forEach((k, v) -> {
                if (k.test(transactions.get(n))) {
                    v.accept(n);
                }
            });
        }
    }

    private Consumer<Integer> createUstAdjuster(final List<BananaTransactionDto> transactions) {
        return new TransactionAdjuster(transactions) {
            @Override
            public void accept(final Integer i) {
                transactions.get(i).setVatCode(VAT_UST_77_CODE);
            }
        };
    }

    private Consumer<Integer> createRoundedAdjuster(final List<BananaTransactionDto> transactions) {
        return new TransactionAdjuster(transactions) {
            @Override
            public void accept(final Integer i) {
                final BananaTransactionDto roundedTransaction = transactions.get(i);
                final BananaTransactionDto mainTransaction = transactions.get(i - 1);
                if (!mainTransaction.getDocument().equals(roundedTransaction.getDocument())) {
                    throw new IllegalArgumentException("The main transaction must belong to the same document as the rounded");
                }
                mainTransaction.setAmountVat(mainTransaction.getAmountVat().add(roundedTransaction.getAmountVat()));
                mainTransaction.setAmountWithoutVat(mainTransaction.getAmountWithoutVat().subtract(roundedTransaction.getAmountVat()));
            }
        };
    }
}
