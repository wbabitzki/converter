package ch.wba.accounting.banana;

import ch.wba.accounting.banana.exception.BananaReaderExceptions;
import ch.wba.accounting.converters.BigDecimalConverter;
import ch.wba.accounting.converters.LocalDateConverter;
import com.opencsv.CSVReader;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class BananaTransactionReader {
    private static final int EXPORT_FIELD_NUMBER = 17;
    private final BananaTransactionPostProcesser bananaTransactionPostProcessor = new BananaTransactionPostProcesser();

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

    // Creates a map with the document identifier as keys and transactions as values
    // The transactions with the same document are added to the first transaction into the composedTransaction list
    protected static final Collector<BananaTransactionDto, ?, LinkedHashMap<String, BananaTransactionDto>> COMPOSED_TRANSACTION_COLLECTOR = //
        Collectors.toMap(BananaTransactionDto::getDocument, // key
            t -> t, // value
            (main, additional) -> {
                main.addIntegratedTransaction(additional);
                return main;
            }, // merge function for the transaction with the same document identifier
            LinkedHashMap::new);

    private static BananaTransactionDto createTransaction(final String[] fields) {
        final Map<Fields, Consumer<BananaTransactionDto>> fieldRules = createFieldRules(fields);
        final BananaTransactionDto transaction = new BananaTransactionDto();
        Arrays.stream(Fields.values()) //
            .forEach(field -> fieldRules.get(field).accept(transaction));
        return transaction;
    }

    private static Map<Fields, Consumer<BananaTransactionDto>> createFieldRules(final String[] fields) {
        final Map<Fields, Consumer<BananaTransactionDto>> fieldRules = new EnumMap<>(Fields.class);
        fieldRules.put(Fields.DATE, dto -> dto.setDate(LocalDateConverter.toDate(fields[Fields.DATE.getIndex()])));
        fieldRules.put(Fields.DOCUMENT, dto -> dto.setDocument(fields[Fields.DOCUMENT.getIndex()]));
        fieldRules.put(Fields.DESCRIPTION, dto -> dto.setDescription(fields[Fields.DESCRIPTION.getIndex()]));
        fieldRules.put(Fields.DEBIT_ACCOUNT, dto -> dto.setDebitAccount(fields[Fields.DEBIT_ACCOUNT.getIndex()]));
        fieldRules.put(Fields.CREDIT_ACCOUNT, dto -> dto.setCreditAccount(fields[Fields.CREDIT_ACCOUNT.getIndex()]));
        fieldRules.put(Fields.TRANSACTION_AMOUNT, dto -> dto.setAmount(BigDecimalConverter.toAmount(fields[Fields.TRANSACTION_AMOUNT.getIndex()])));
        fieldRules.put(Fields.VAT_CODE, dto -> {
            final String vatCode = fields[Fields.VAT_CODE.getIndex()];
            if (StringUtils.isNotEmpty(vatCode)) {
                dto.setVatCode(vatCode);
            }
        });
        fieldRules.put(Fields.VAT_PCT, dto -> {
            final String vatPct = fields[Fields.VAT_PCT.getIndex()];
            if (StringUtils.isNotEmpty(vatPct)) {
                dto.setVatPct(BigDecimalConverter.toPct(vatPct));
            }
        });
        fieldRules.put(Fields.AMOUNT_WITHOUT_VAT, dto -> {
            final String amountWithoutVat = fields[Fields.AMOUNT_WITHOUT_VAT.getIndex()];
            if (StringUtils.isNotEmpty(amountWithoutVat)) {
                dto.setAmountWithoutVat(BigDecimalConverter.toAmount(amountWithoutVat));
            }
        });
        fieldRules.put(Fields.AMOUNT_VAT, dto -> {
            final String amountVat = fields[Fields.AMOUNT_VAT.getIndex()];
            if (StringUtils.isNotEmpty(amountVat)) {
                dto.setAmountVat(BigDecimalConverter.toAmount(amountVat));
            }
        });
        fieldRules.put(Fields.VAT_ACCOUNT, dto -> dto.setVatAccount(fields[Fields.VAT_ACCOUNT.getIndex()]));
        return fieldRules;
    }

    public List<BananaTransactionDto> readTransactions(final BufferedReader buffer, final char delimiter) {
        Validate.isTrue(buffer != null);
        final List<String> lines = buffer.lines().collect(Collectors.toList());
        Validate.notEmpty(lines, "The imported list ist empty");
        //
        final List<BananaTransactionDto> initialTransactions = mapFromString(lines, delimiter);
        final List<BananaTransactionDto> adjustedTransactions = bananaTransactionPostProcessor.adjustTransactions(initialTransactions);
        return new ArrayList<>(adjustedTransactions.stream() //
                .collect(COMPOSED_TRANSACTION_COLLECTOR) //
                .values());
    }

    public List<BananaTransactionDto> readTransactions(final BufferedReader buffer) {
       return readTransactions(buffer, '\t');
    }

    protected List<BananaTransactionDto> mapFromString(final List<String> lines, char delimiter) {
        return lines.stream() //
                .map(line -> toBananaDto(line, delimiter)) //
                .collect(Collectors.toList());
    }

    protected BananaTransactionDto toBananaDto(String line, char delimiter) {
        String[] fields = null;
        try (CSVReader csvReader = createCsvReader(line, delimiter)) {
            fields = csvReader.readNext();
        } catch (final IOException e) {
            throw new BananaReaderExceptions.InvalidLineException(String.format("Invalid line: \"%s\"", line));
        }
        validateFields(line, fields);
        return createTransaction(fields);
    }

    private void validateFields(String line, String[] fields) {
        if (fields == null || fields.length < EXPORT_FIELD_NUMBER) {
            final String message = String.format("Invalid fields number. Expected %d but was %d", EXPORT_FIELD_NUMBER,
                    fields == null ? 0 : fields.length);
            throw new BananaReaderExceptions.InvalidFieldNumberException(message);
        }
        final String date = fields[Fields.DATE.ordinal()];
        if (!LocalDateConverter.isDate(date)) {
            throw new BananaReaderExceptions.InvalidDateException(String.format("Invalid date: \"%s\" in line \"%s\"", date, line));
        }
    }

    protected CSVReader createCsvReader(String line, char delimiter) {
        return new CSVReader(new StringReader(line), delimiter);
    }
}
