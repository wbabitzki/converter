package ch.wba.accounting.banana;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ch.wba.accounting.banana.BananaTransactionPredicates.isRounded;

public class BananaTransactionPostProcessor {
    final private static List<PostProcessorRule> rules = Arrays.asList(new UstRule(), new RoundedRule());

    public List<BananaTransactionDto> adjustTransactions(final List<BananaTransactionDto> transactions) {
        return applyRules(transactions).stream()
                .filter(isRounded.negate())
                .collect(Collectors.toList());
    }

    private List<BananaTransactionDto> applyRules(final List<BananaTransactionDto> transactions) {
        IntStream.range(0, transactions.size()).forEach(i -> {
            for (PostProcessorRule rule : rules) {
                rule.conditionalApply(i, transactions);
            }
        });
        return transactions;
    }
}
