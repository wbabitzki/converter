package ch.wba.accounting;

import static ch.wba.accounting.converters.BigDecimalConverter.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.StringJoiner;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import ch.wba.accounting.converters.LocalDateConverter;

public class AccountTransactionDto {
    private String receipt;
    private LocalDate transactionDate;
    private String targetAccount;
    private String sourceAccount;
    private String sId;
    private int sIdx;
    private BigDecimal totalAmount;
    private String description;
    private BigDecimal tax;
    private BigDecimal amountBeforeTax;

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(final String receipt) {
        this.receipt = receipt;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(final LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setTransactionDate(final String transactionDate) {
        this.transactionDate = LocalDateConverter.toDate(transactionDate);
    }

    public String getTargetAccount() {
        return targetAccount;
    }

    public void setTargetAccount(final String targetAccount) {
        this.targetAccount = targetAccount;
    }

    public String getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(final String sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(final String sId) {
        this.sId = sId;
    }

    public int getsIdx() {
        return sIdx;
    }

    public void setsIdx(final int sIdx) {
        this.sIdx = sIdx;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(final BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setAmountBeforeTax(final BigDecimal amountBeforeTax) {
        this.amountBeforeTax = amountBeforeTax;
    }

    public BigDecimal getAmountBeforeTax() {
        return amountBeforeTax;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(final BigDecimal tax) {
        this.tax = tax;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder() //
            .append(receipt) //
            .append(transactionDate) //
            .append(targetAccount) //
            .append(sourceAccount) // 
            .append(totalAmount) //
            .append(description) //
            .append(tax) //
            .toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final AccountTransactionDto that = (AccountTransactionDto) obj;
        return new EqualsBuilder() //
            .append(receipt, that.receipt) //
            .append(transactionDate, that.transactionDate) //
            .append(targetAccount, that.targetAccount) //
            .append(sourceAccount, that.sourceAccount) //
            .append(totalAmount, that.totalAmount) //
            .append(description, that.description) //
            .append(tax, that.tax) //
            .isEquals();
    }

    @Override
    public String toString() {
        return new StringJoiner(";") //
            .add(receipt == null ? "" : receipt) //
            .add(transactionDate == null ? "" : LocalDateConverter.toString(transactionDate))//
            .add(targetAccount == null ? "" : targetAccount) //
            .add(sourceAccount == null ? "" : sourceAccount) //
            .add(totalAmount == null ? "" : asString(totalAmount)) //
            .add(amountBeforeTax == null ? "" : asString(amountBeforeTax)) //
            .add(tax == null ? "" : asString(tax)) //
            .add(description == null ? "" : description) //
            .toString();
    }
}
