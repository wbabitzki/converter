package ch.wba.accounting.sega;

import ch.wba.accounting.banana.BananaTransactionDto;
import ch.wba.accounting.sega.converter.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ConverterService {

    public List<SegaDto> convert(List<BananaTransactionDto> bananaTransaction) {
        ConverterFactory converterFactory = new ConverterFactory();
        converterFactory.register(ConverterService::isExpensesWithVat, new SegaExpensesWithVatConverter());
        converterFactory.register(ConverterService::isIncomeWithVat, new SegaIncomeWithVatConverter());
        converterFactory.register(ConverterService::isWithoutVat, new SegaWithoutTaxConverter());
        converterFactory.register(ConverterService::isComposed, new SegaComposedConverter());

        List<SegaDto> segaDtos = new ArrayList<>();
        for (BananaTransactionDto transaction : bananaTransaction) {
            SegaConverter converter = converterFactory.create(transaction);
            segaDtos.addAll(converter.toSegaTransactions(transaction));
        }

        return segaDtos;
    }

    protected static boolean isExpensesWithVat(BananaTransactionDto transaction) {
        return !isWithoutVat(transaction) && !isComposed(transaction)
                && BigDecimal.ZERO.compareTo(transaction.getAmountVat()) < 0;
    }

    protected static boolean isIncomeWithVat(BananaTransactionDto transaction) {
        return !isWithoutVat(transaction) && !isComposed(transaction)
                && BigDecimal.ZERO.compareTo(transaction.getAmountVat()) > 0;
    }

    protected static boolean isWithoutVat(BananaTransactionDto transaction) {
        return !isComposed(transaction) && transaction.getAmountVat() == null;
    }

    protected static boolean isComposed(BananaTransactionDto transaction) {
        return transaction.isComposedTransaction();
    }
}
