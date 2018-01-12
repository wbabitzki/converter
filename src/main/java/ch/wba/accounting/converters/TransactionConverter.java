package ch.wba.accounting.converters;

import ch.wba.accounting.AccountTransactionDto;
import ch.wba.accounting.ubs.UbsTransactionDto;
import org.apache.commons.lang.Validate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TransactionConverter {

    private static final BigDecimal TAX_PERCENTAGE = new BigDecimal("0.08");

    public List<AccountTransactionDto> convert(List<UbsTransactionDto> ubsTransactions) {
        Validate.notNull(ubsTransactions);
        AtomicInteger count = new AtomicInteger(1);
        return ubsTransactions.stream()
                .filter(isValuableTransaction())
                .map(mapTransaction(count))
                .collect(Collectors.toList());
    }

    private Predicate<UbsTransactionDto> isValuableTransaction() {
        return ubsTransactionDto ->
                ubsTransactionDto.getBalance() != null
                        && (isValidAmount(ubsTransactionDto.getDebit()) || isValidAmount(ubsTransactionDto.getCredit()));
    }

    private Function<UbsTransactionDto, AccountTransactionDto> mapTransaction(AtomicInteger count) {
        return source -> {
            AccountTransactionDto result = new AccountTransactionDto();
            result.setReceipt(Integer.toString(count.getAndIncrement()));
            result.setTransactionDate(source.getTradeDate());
            if (isValidAmount(source.getCredit())) {
                result.setTotalAmount(source.getCredit());
            } else if (isValidAmount(source.getDebit())) {
                result.setTotalAmount(source.getDebit());
            }
            result.setDescription(source.getDescription2());
            result.setTax(calculatePercentage(result.getTotalAmount(), TAX_PERCENTAGE));
            result.setAmountBeforeTax(result.getTotalAmount().subtract(result.getTax()));
            return result;
        };
    }

    private boolean isValidAmount(BigDecimal amount) {
        return amount != null && !amount.equals(BigDecimal.ZERO);
    }

    private static BigDecimal calculatePercentage(BigDecimal total, BigDecimal percentage) {
        return total
                .divide(percentage.add(BigDecimal.ONE), 2, RoundingMode.HALF_UP)
                .multiply(percentage).setScale(2, RoundingMode.HALF_UP);
    }
}
