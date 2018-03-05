package ch.wba.accounting.banana;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import ch.wba.accounting.converters.BigDecimalConverter;
import ch.wba.accounting.converters.LocalDateConverter;

import com.opencsv.CSVReader;

public class BananaTransactionReader {
    private static final int EXPORT_FIELD_NUMBER = 17;

    private enum Fields {
            DATE(0), //
            DOCUMENT(1), //
            DESCRIPTION(2), //
            DEBIT_ACCOUNT(3), //
            CREDIT_ACCOUNT(4), //
            TRANSACTION_AMOUNT(5), //
            VAT_CODE(6), //
            VAT_PCT(8), //
            AMOUNT_WITHOUT_VAT(10), //
            AMOUNT_VAT(11), //
            VAT_ACCOUNT(12);
        private final int index;

        Fields(final int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    protected static final Function<String, BananaTransactionDto> MAPPER = line -> {
        String[] fields;
        final StringReader reader = new StringReader(line);
        try (CSVReader csvReader = new CSVReader(reader)) {
            fields = csvReader.readNext();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        if (fields == null || fields.length != EXPORT_FIELD_NUMBER) {
            return null;
        }
        if (!LocalDateConverter.isDate(fields[Fields.DATE.ordinal()])) {
            return null;
        }
        return createTransaction(fields);
    };
    protected static final Collector<BananaTransactionDto, ?, LinkedHashMap<String, BananaTransactionDto>> COMPOSED_TRANSACTION_COLLECTOR = Collectors.toMap(BananaTransactionDto::getDocument, //
        t -> t, //
        (main, additional) -> {
            main.addComposedTransaction(additional);
            return main;
        }, //
        LinkedHashMap::new);

    private static BananaTransactionDto createTransaction(final String[] fields) {
        final BananaTransactionDto transaction = new BananaTransactionDto();
        transaction.setDate(LocalDateConverter.toDate(fields[Fields.DATE.getIndex()]));
        transaction.setDocument(fields[Fields.DOCUMENT.getIndex()]);
        transaction.setDescription(fields[Fields.DESCRIPTION.getIndex()]);
        transaction.setDebitAccount(fields[Fields.DEBIT_ACCOUNT.getIndex()]);
        transaction.setCreditAccount(fields[Fields.CREDIT_ACCOUNT.getIndex()]);
        transaction.setAmount(BigDecimalConverter.toAmount(fields[Fields.TRANSACTION_AMOUNT.getIndex()]));
        final String vatCode = fields[Fields.VAT_CODE.getIndex()];
        if (StringUtils.isNotEmpty(vatCode)) {
            transaction.setVatCode(vatCode);
        }
        final String vatPct = fields[Fields.VAT_PCT.getIndex()];
        if (StringUtils.isNotEmpty(vatPct)) {
            transaction.setVatPct(BigDecimalConverter.toAmount(vatPct));
        }
        final String amountWithoutVat = fields[Fields.AMOUNT_WITHOUT_VAT.getIndex()];
        if (StringUtils.isNotEmpty(amountWithoutVat)) {
            transaction.setAmountWithoutVat(BigDecimalConverter.toAmount(amountWithoutVat));
        }
        final String amountVat = fields[Fields.AMOUNT_VAT.getIndex()];
        if (StringUtils.isNotEmpty(amountVat)) {
            transaction.setAmountVat(BigDecimalConverter.toAmount(amountVat));
        }
        transaction.setVatAccount(fields[Fields.VAT_ACCOUNT.getIndex()]);
        return transaction;
    }

    public List<BananaTransactionDto> readTransactions(final BufferedReader buffer) {
        Validate.notNull(buffer);
        final List<String> lines = buffer.lines().collect(Collectors.toList());
        Validate.notEmpty(lines, "The imported list ist empty");
        final Map<String, BananaTransactionDto> transactions = lines.stream() //
            .map(MAPPER) //
            .filter(Objects::nonNull) //
            .collect(COMPOSED_TRANSACTION_COLLECTOR);
        return new ArrayList<>(transactions.values());
    }
}
