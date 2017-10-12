package ch.wba.account.ubs;

import org.apache.commons.lang.Validate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UbsTransactionReader {

    @FunctionalInterface
    private interface InitFieldFunction<T extends UbsTransactionDto, R extends String> {
        void apply(T entity, R fields);
    }
    
    enum Header {
        VALUATION_DATE("Valuation date", UbsTransactionDto::setValuationDate), //
        BANKING_RELATIONSHIP("Banking relationship", UbsTransactionDto::setBankingRelationship), //
        PORTFOLIO("Portfolio", UbsTransactionDto::setPortfolio), //
        PRODUCT("Product", UbsTransactionDto::setProduct),   //
        IBAN("IBAN", UbsTransactionDto::setIban), //
        CCY("Ccy.", UbsTransactionDto::setCcy), //
        DATE_FROM("Date from", UbsTransactionDto::setFromDate), //
        DATE_TO("Date to", UbsTransactionDto::setToDate), //
        DESCRIPTION("Description", UbsTransactionDto::setDescription), //
        TRADE_DATE("Trade date", UbsTransactionDto::setTradeDate), //
        BOOKING_DATE("Booking date", UbsTransactionDto::setBookingDate), //
        VALUE_DATE("Value date", UbsTransactionDto::setValueDate), //
        DESCRIPTION_1("Description 1", UbsTransactionDto::setDescription1), //
        DESCRIPTION_2("Description 2", UbsTransactionDto::setDescription2), //
        DESCRIPTION_3("Description 3", UbsTransactionDto::setDescription3), //
        TRANSACTION_NO("Transaction no.", UbsTransactionDto::setTransactionNo), //
        EXCHANGE_RATE("Exchange rate in the original amount in settlement currency", UbsTransactionDto::setExcangeRate), //
        INDIVIDUAL_AMOUNT("Individual amount", UbsTransactionDto::setAmount), //
        DEBIT("Debit", UbsTransactionDto::setDebit), //
        CREDIT("Credit", UbsTransactionDto::setCredit), //
        BALANCE("Balance", UbsTransactionDto::setBalance);
        
        private final String value;
        private final InitFieldFunction<UbsTransactionDto, String> initFieldFunction;

        Header(final String value, final InitFieldFunction<UbsTransactionDto, String> function) {
            this.value = value;
            this.initFieldFunction = function;
        }

        private String getValue() {
            return value;
        }

        private void setField(final UbsTransactionDto entity, final String text) {
            if (!text.isEmpty()) {
                initFieldFunction.apply(entity, text);
            }
        }
    }

    private static final Function<String, UbsTransactionDto> ENTITY_MAPPER = line -> {
        final String[] fields = line.split(";");
        final UbsTransactionDto entity = new UbsTransactionDto();

        for (Header header : Header.values()) {
            header.setField(entity, fields[header.ordinal()]);
        }

        return entity;
    };

    public List<UbsTransactionDto> read(final InputStream input) throws IOException {
        Validate.notNull(input);
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            final List<String> lines = buffer.lines().collect(Collectors.toList());
            validateHeader(lines);
            return lines //
                    .subList(1, lines.size()).stream() //
                    .map(ENTITY_MAPPER) //
                    .collect(Collectors.toList());
        }
    }

    private void validateHeader(final List<String> lines) {
        Validate.notEmpty(lines, "The imported list ist empty");
        final String[] headers = lines.get(0).split(";");
        for (final Header header : Header.values()) {
            final String headerName = headers[header.ordinal()];
            Validate.isTrue(header.getValue().equals(headerName), "Unexpected header: '" + headerName + "' instead of '" + header.getValue() + "'");
        }
    }
}
