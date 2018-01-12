package ch.wba.accounting.ubs;

import ch.wba.accounting.converters.BigDecimalConverter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.math.BigDecimal;

public class UbsBalanceDto {
    private BigDecimal openingBalance;
    private BigDecimal closingBalance;

    public BigDecimal getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(BigDecimal openingBalance) {
        this.openingBalance = openingBalance;
    }

    public void setOpeningBalance(String openingBalance) {
        this.openingBalance = BigDecimalConverter.toAmount(openingBalance);
    }

    public BigDecimal getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(BigDecimal closingBalance) {
        this.closingBalance = closingBalance;
    }

    public void setClosingBalance(String closingBalance) {
        this.closingBalance = BigDecimalConverter.toAmount(closingBalance);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(openingBalance)
                .append(closingBalance)
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
        final UbsBalanceDto that = (UbsBalanceDto) obj;

        return new EqualsBuilder()
                .append(openingBalance, that.openingBalance)
                .append(closingBalance, that.closingBalance)
                .isEquals();
    }
}
