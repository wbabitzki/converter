package ch.wba.accounting.banana;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import ch.wba.accounting.converters.BigDecimalConverter;
import ch.wba.accounting.converters.LocalDateConverter;

import com.opencsv.CSVReader;

public class BananaTransactionReader {
    protected static final String VAT_UP_ROUNDED_CODE = "M77-2";
    protected static final String VAT_OFF_ROUNDED_CODE = "-M77-2";
    protected static final String VAT_UST_77_CODE = "USt77";
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

    protected static final Function<String, BananaTransactionDto> STRING_TO_DTO_MAPPER = line -> {
        String[] fields = null;
        try (CSVReader csvReader = new CSVReader(new StringReader(line))) {
            fields = csvReader.readNext();
        } catch (final IOException e) {
            System.err.println("A line cannot be read: " + e.getMessage());
        }
        if (fields == null || fields.length != EXPORT_FIELD_NUMBER) {
            return null;
        }
        if (!LocalDateConverter.isDate(fields[Fields.DATE.ordinal()])) {
            return null;
        }
        return createTransaction(fields);
    };
    //
    // Creates a map with the document identifier as keys and transactions as values
    // The transactions with the same document are added to the first transaction into the composedTransaction list
    protected static final Collector<BananaTransactionDto, ?, LinkedHashMap<String, BananaTransactionDto>> COMPOSED_TRANSACTION_COLLECTOR = //
        Collectors.toMap(BananaTransactionDto::getDocument, // key
            t -> t, // value
            (main, additional) -> {
                main.addComposedTransaction(additional);
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

    public List<BananaTransactionDto> readTransactions(final BufferedReader buffer) {
        Validate.isTrue(buffer != null);
        final List<String> lines = buffer.lines().collect(Collectors.toList());
        Validate.notEmpty(lines, "The imported list ist empty");
        //
        final List<BananaTransactionDto> initialTransactions = mapFromString(lines);
        final List<BananaTransactionDto> adjustedTransactions = adjustTransactions(initialTransactions);
        return new ArrayList<>(adjustedTransactions.stream() //
            .collect(COMPOSED_TRANSACTION_COLLECTOR) //
            .values());
    }

    private List<BananaTransactionDto> mapFromString(final List<String> lines) {
        return lines.stream() //
            .map(STRING_TO_DTO_MAPPER) //
            .filter(Objects::nonNull) //
            .collect(Collectors.toList());
    }

    protected List<BananaTransactionDto> adjustTransactions(final List<BananaTransactionDto> transactions) {
        final ArrayList<BananaTransactionDto> roundedVatTransactions = new ArrayList<>();
        for (int i = 0; i < transactions.size(); i++) {
            final String vatCode = transactions.get(i).getVatCode();
            if (VAT_UP_ROUNDED_CODE.equals(vatCode) || VAT_OFF_ROUNDED_CODE.equals(vatCode)) {
                final BananaTransactionDto roundedTransaction = transactions.get(i);
                final BananaTransactionDto mainTransaction = transactions.get(i - 1);
                if (!mainTransaction.getDocument().equals(roundedTransaction.getDocument())) {
                    throw new IllegalArgumentException("The main transaction must belong to the same document as the rounded");
                }
                roundedVatTransactions.add(roundedTransaction);
                mainTransaction.setAmountVat(mainTransaction.getAmountVat().add(roundedTransaction.getAmountVat()));
                mainTransaction.setAmountWithoutVat(mainTransaction.getAmountWithoutVat().subtract(roundedTransaction.getAmountVat()));
            } else if (VAT_UST_77_CODE.equalsIgnoreCase(vatCode)) {
                transactions.get(i).setVatCode(VAT_UST_77_CODE);
            }
        }
        return transactions.stream() //
            .filter(t -> !roundedVatTransactions.contains(t)) //
            .collect(Collectors.toList());
    }
}
