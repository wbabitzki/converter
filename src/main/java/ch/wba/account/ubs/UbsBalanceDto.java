package ch.wba.account.ubs;

import converters.BigDecimalConverter;

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
}
