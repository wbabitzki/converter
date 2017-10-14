package ch.wba.account.ubs;


import converters.BigDecimalConverter;
import converters.LocalDateConverter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;


public class UbsTransactionDto {
    private LocalDate valuationDate;
    private String bankingRelationship;
    private String portfolio;
    private String product;
    private String iban;
    private String ccy;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String description;
    private LocalDate tradeDate;
    private LocalDate bookingDate;
    private LocalDate valueDate;
    private String description1;
    private String description2;
    private String description3;
    private String transactionNo;
    private BigDecimal excandeRate;
    private BigDecimal amount;
    private BigDecimal debit;
    private BigDecimal credit;
    private BigDecimal balance;

    public LocalDate getValuationDate() {
        return valuationDate;
    }

    public void setValuationDate(LocalDate valuationDate) {
        this.valuationDate = valuationDate;
    }

    public void setValuationDate(String valuationDate) {
        this.valuationDate = LocalDateConverter.toDate(valuationDate);
    }

    public String getBankingRelationship() {
        return bankingRelationship;
    }

    public void setBankingRelationship(String bankingRelationship) {
        this.bankingRelationship = bankingRelationship;
    }

    public String getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(String portfolio) {
        this.portfolio = portfolio;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getCcy() {
        return ccy;
    }

    public void setCcy(String ccy) {
        this.ccy = ccy;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = LocalDateConverter.toDate(fromDate);
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }
    public void setToDate(String  toDate) {
        this.toDate = LocalDateConverter.toDate(toDate);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(LocalDate tradeDate) {
        this.tradeDate = tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = LocalDateConverter.toDate(tradeDate);
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = LocalDateConverter.toDate(bookingDate);
    }

    public LocalDate getValueDate() {
        return valueDate;
    }

    public void setValueDate(LocalDate valueDate) {
        this.valueDate = valueDate;
    }

    public void setValueDate(String valueDate) {
        this.valueDate = LocalDateConverter.toDate(valueDate);
    }

    public String getDescription1() {
        return description1;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }

    public String getDescription2() {
        return description2;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public String getDescription3() {
        return description3;
    }

    public void setDescription3(String description3) {
        this.description3 = description3;
    }

    public String  getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public BigDecimal getExcandeRate() {
        return excandeRate;
    }

    public void setExcangeRate(BigDecimal excandeRate) {
        this.excandeRate = excandeRate;
    }

    public void setExcangeRate(String excangeRate) {
        this.excandeRate = BigDecimalConverter.toAmount(excangeRate);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public void setAmount(String amount) {
        this.amount = BigDecimalConverter.toAmount(amount);
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public void setDebit(String debit) {
        this.debit = BigDecimalConverter.toAmount(debit);
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public void setCredit(String credit) {
        this.credit = BigDecimalConverter.toAmount(credit);
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setBalance(String balance) {
        this.balance = BigDecimalConverter.toAmount(balance);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(amount)
                .append(balance)
                .append(bankingRelationship)
                .append(bookingDate)
                .append(ccy)
                .append(credit)
                .append(debit)
                .append(description1)
                .append(description2)
                .append(description3)
                .append(description)
                .append(excandeRate)
                .append(fromDate)
                .append(iban)
                .append(portfolio)
                .append(product)
                .append(toDate)
                .append(tradeDate)
                .append(transactionNo)
                .append(valueDate)
                .append(valuationDate)
                .hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final UbsTransactionDto that = (UbsTransactionDto) obj;

        return new EqualsBuilder()
                .append(amount, that.amount)
                .append(balance, that.balance)
                .append(bankingRelationship, that.bankingRelationship)
                .append(bookingDate, that.bookingDate)
                .append(ccy, that.ccy)
                .append(credit, that.credit)
                .append(debit, that.debit)
                .append(description1, that.description1)
                .append(description2, that.description2)
                .append(description3, that.description3)
                .append(description, that.description)
                .append(excandeRate, that.excandeRate)
                .append(fromDate, that.fromDate)
                .append(iban, that.iban)
                .append(portfolio, that.portfolio)
                .append(product, that.product)
                .append(toDate, that.toDate)
                .append(tradeDate, that.tradeDate)
                .append(transactionNo, that.transactionNo)
                .append(valueDate, that.valueDate)
                .append(valuationDate, that.valuationDate)
                .isEquals();
    }
}
