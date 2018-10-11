package ch.wba.accounting.sega;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import ch.wba.accounting.banana.BananaTransactionDto;
import ch.wba.accounting.sega.converter.SegaComposedConverter;
import ch.wba.accounting.sega.converter.SegaExpensesWithVatConverter;
import ch.wba.accounting.sega.converter.SegaIncomeWithVatConverter;
import ch.wba.accounting.sega.converter.SegaWithoutTaxConverter;

public class ConverterService {
    private final ConverterFactory converterFactory = new ConverterFactory();

    public ConverterService() {
        converterFactory.register(ConverterService::isExpensesWithVat, new SegaExpensesWithVatConverter());
        converterFactory.register(ConverterService::isIncomeWithVat, new SegaIncomeWithVatConverter());
        converterFactory.register(ConverterService::isWithoutVat, new SegaWithoutTaxConverter());
        converterFactory.register(ConverterService::isComposed, new SegaComposedConverter());
    }

    public List<SegaDto> convert(final List<BananaTransactionDto> bananaTransaction) {
        return bananaTransaction.stream() //
            .map(t -> converterFactory.create(t).toSegaTransactions(t)) //
            .flatMap(List::stream) //
            .collect(Collectors.toList());
    }

    protected static boolean isExpensesWithVat(final BananaTransactionDto transaction) {
        return !isWithoutVat(transaction) && !isComposed(transaction) && BigDecimal.ZERO.compareTo(transaction.getAmountVat()) < 0;
    }

    protected static boolean isIncomeWithVat(final BananaTransactionDto transaction) {
        return !isWithoutVat(transaction) && !isComposed(transaction) && BigDecimal.ZERO.compareTo(transaction.getAmountVat()) > 0;
    }

    protected static boolean isWithoutVat(final BananaTransactionDto transaction) {
        return !isComposed(transaction) && transaction.getAmountVat() == null;
    }

    protected static boolean isComposed(final BananaTransactionDto transaction) {
        return transaction.isComposedTransaction();
    }
}
