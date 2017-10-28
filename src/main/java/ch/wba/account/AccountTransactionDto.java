package ch.wba.account;

import ch.wba.account.converters.LocalDateConverter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.StringJoiner;

import static ch.wba.account.converters.BigDecimalConverter.asString;

public class AccountTransactionDto {

    private String receipt;
    private LocalDate transactionDate;
    private String targetAccount;
    private String sourceAccount;
    private BigDecimal totalAmount;
    private String description;
    private BigDecimal tax;
    private BigDecimal amountBeforeTax;

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = LocalDateConverter.toDate(transactionDate);
    }

    public String getTargetAccount() {
        return targetAccount;
    }

    public void setTargetAccount(String targetAccount) {
        this.targetAccount = targetAccount;
    }

    public String getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmountBeforeTax(BigDecimal amountBeforeTax) {
        this.amountBeforeTax = amountBeforeTax;
    }

    public BigDecimal getAmountBeforeTax() {
        return amountBeforeTax;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(receipt)
            .append(transactionDate)
            .append(targetAccount)
            .append(sourceAccount)
            .append(totalAmount)
            .append(description)
            .append(tax)
            .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final AccountTransactionDto that = (AccountTransactionDto) obj;

        return new EqualsBuilder()
            .append(receipt, that.receipt)
            .append(transactionDate, that.transactionDate)
            .append(targetAccount, that.targetAccount)
            .append(sourceAccount, that.sourceAccount)
            .append(totalAmount, that.totalAmount)
            .append(description, that.description)
            .append(tax, that.tax)
            .isEquals();
    }

    @Override
    public String toString() {
        return new StringJoiner(";")
                .add(receipt == null ? "" : receipt)
                .add(transactionDate == null ? "" : LocalDateConverter.toString(transactionDate))
                .add(targetAccount == null ? "" : targetAccount)
                .add(sourceAccount == null ? "" : sourceAccount)
                .add(totalAmount == null ? "" : asString(totalAmount))
                .add(amountBeforeTax == null ? "" : asString(amountBeforeTax))
                .add(tax == null ? "" : asString(tax))
                .add(description == null ? "" : description)
                .toString();
    }

}
