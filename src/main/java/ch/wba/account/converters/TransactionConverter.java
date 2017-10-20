package ch.wba.account.converters;

import ch.wba.account.TransactionDto;
import ch.wba.account.ubs.UbsTransactionDto;
import org.apache.commons.lang.Validate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TransactionConverter {

    private static final BigDecimal TAX_PERCENTAGE = new BigDecimal(0.08);

    List<TransactionDto> convert(List<UbsTransactionDto> ubsTransactions) {
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
                        && (ubsTransactionDto.getDebit() != null || ubsTransactionDto.getCredit() != null);
    }

    private Function<UbsTransactionDto, TransactionDto> mapTransaction(AtomicInteger count) {
        return source -> {
            TransactionDto result = new TransactionDto();
            result.setReceipt(Integer.toString(count.getAndIncrement()));
            result.setTransactionDate(source.getTradeDate());
            if (source.getCredit() != null) {
                result.setTotalAmount(source.getCredit());
            } else if (source.getDebit() != null) {
                result.setTotalAmount(source.getDebit());
            }
            result.setDescription(source.getDescription2());
            result.setTax(calculatePercentage(result.getTotalAmount(), TAX_PERCENTAGE));
            return result;
        };
    }

    private static BigDecimal calculatePercentage(BigDecimal total, BigDecimal percentage) {
        return total
                .divide(percentage.add(BigDecimal.ONE), 2, RoundingMode.HALF_UP)
                .multiply(percentage).setScale(2, RoundingMode.HALF_UP);
    }
}
