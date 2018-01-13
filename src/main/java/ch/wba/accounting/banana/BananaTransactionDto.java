package ch.wba.accounting.banana;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BananaTransactionDto {
    private LocalDate date;
    private String document;
    private String description;
    private String debitAccount;
    private String creditAccount;
    private BigDecimal amount;
    private String vatCode;
    private BigDecimal vatPct;
    private BigDecimal amountWithoutVat;
    private BigDecimal amountVat;
    private String taxAccount;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getVatCode() {
        return vatCode;
    }

    public void setVatCode(String vatCode) {
        this.vatCode = vatCode;
    }

    public BigDecimal getVatPct() {
        return vatPct;
    }

    public void setVatPct(BigDecimal vatPct) {
        this.vatPct = vatPct;
    }

    public BigDecimal getAmountWithoutVat() {
        return amountWithoutVat;
    }

    public void setAmountWithoutVat(BigDecimal amountWithoutVat) {
        this.amountWithoutVat = amountWithoutVat;
    }

    public BigDecimal getAmountVat() {
        return amountVat;
    }

    public void setAmountVat(BigDecimal amountVat) {
        this.amountVat = amountVat;
    }

    public String getTaxAccount() {
        return taxAccount;
    }

    public void setTaxAccount(String taxAccount) {
        this.taxAccount = taxAccount;
    }
}