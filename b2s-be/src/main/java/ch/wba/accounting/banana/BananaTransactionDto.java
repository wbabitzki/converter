package ch.wba.accounting.banana;

import ch.wba.accounting.banana.validation.AmoundConstraint;
import ch.wba.accounting.banana.validation.ComposedTransactionSumConstraint;
import ch.wba.accounting.banana.validation.FromAccountConstraint;
import ch.wba.accounting.banana.validation.IntegratedTransactionDateConstraint;
import ch.wba.accounting.banana.validation.ToAccountConstraint;
import ch.wba.accounting.banana.validation.WithVatChecks;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@FromAccountConstraint
@ToAccountConstraint
@ComposedTransactionSumConstraint
@IntegratedTransactionDateConstraint
@AmoundConstraint
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "uuid")
public class BananaTransactionDto {
    private final UUID uuid;
    @NotNull
    private LocalDate date;
    @NotBlank
    private String document = "";
    @NotBlank
    private String description = "";
    private String debitAccount = "";
    private String creditAccount = "";
    @NotNull
    private BigDecimal amount;
    @NotBlank(groups = WithVatChecks.class)
    private String vatCode = "";
    @NotNull(groups = WithVatChecks.class)
    private BigDecimal vatPct;
    @NotNull(groups = WithVatChecks.class)
    private BigDecimal amountWithoutVat;
    @NotNull(groups = WithVatChecks.class)
    private BigDecimal amountVat;
    @NotBlank(groups = WithVatChecks.class)
    private String vatAccount = "";
    private final List<BananaTransactionDto> integratedTransactions = new ArrayList<>();
    private BananaTransactionDto mainTransaction;

    public BananaTransactionDto() {
        uuid = UUID.randomUUID();
    }

    public UUID getUuid() {
        return uuid;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(final LocalDate date) {
        this.date = date;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(final String document) {
        this.document = document;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(final String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(final String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public String getVatCode() {
        return vatCode;
    }

    public void setVatCode(final String vatCode) {
        this.vatCode = vatCode;
    }

    public BigDecimal getVatPct() {
        return vatPct;
    }

    public void setVatPct(final BigDecimal vatPct) {
        this.vatPct = vatPct;
    }

    public BigDecimal getAmountWithoutVat() {
        return amountWithoutVat;
    }

    public void setAmountWithoutVat(final BigDecimal amountWithoutVat) {
        this.amountWithoutVat = amountWithoutVat;
    }

    public BigDecimal getAmountVat() {
        return amountVat;
    }

    public void setAmountVat(final BigDecimal amountVat) {
        this.amountVat = amountVat;
    }

    public String getVatAccount() {
        return vatAccount;
    }

    public void setVatAccount(final String vatAccount) {
        this.vatAccount = vatAccount;
    }

    public List<BananaTransactionDto> getIntegratedTransactions() {
        return Collections.unmodifiableList(integratedTransactions);
    }

    public void addIntegratedTransaction(final BananaTransactionDto transaction) {
        transaction.mainTransaction = this;
        this.integratedTransactions.add(transaction);
    }

    public boolean isComposedTransaction() {
        return !this.integratedTransactions.isEmpty();
    }

    public boolean isWithVat() {
        final boolean isVatCodeEmpty = Optional.ofNullable(vatCode).filter(String::isEmpty).isPresent();
        return !isVatCodeEmpty || vatPct != null || amountWithoutVat != null || amountVat != null;
    }

    public boolean isIntegrated() {
        return this.mainTransaction != null;
    }

    public BananaTransactionDto getMainTransaction() {
        return mainTransaction;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(document).append(description).toString();
    }
}