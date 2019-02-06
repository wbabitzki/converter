package ch.wba.accounting.sega;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.StringJoiner;

import ch.wba.accounting.converters.BigDecimalConverter;
import ch.wba.accounting.converters.LocalDateConverter;

public class SegaDto {
    protected static final String[] HEADERS = { "Blg", "Datum", "Kto", "S/H", "Grp", "GKto", "SId", "SIdx", "KIdx", "BTyp", "MTyp", "Code", //
        "Netto", "Steuer", "FW-Betrag", "Tx1", "Tx2", "PkKey", "OpId", "Flag" };
    protected static final String DELIMITER = ",";
    private static final String QUOTE_SYMBOL = "\"";

    public enum SOLL_HABEN {
            SOLL("S"), //
            HABEN("H");
        private final String code;

        SOLL_HABEN(final String code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return code;
        }
    }

    private String blg;
    private LocalDate datum;
    private String kto;
    private SOLL_HABEN transactionType;
    private String grp;
    private String gKto;
    private String sId;
    private int sIdx;
    private int kIndx;
    private int bType;
    private int mType;
    private String code;
    private BigDecimal netto;
    private BigDecimal steuer;
    private BigDecimal fwBetrag;
    private String tx1;
    private String tx2;
    private int pkKey;
    private String opId;
    private int flag;

    public String getBlg() {
        return blg;
    }

    public void setBlg(final String blg) {
        this.blg = blg;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(final LocalDate datum) {
        this.datum = datum;
    }

    public String getKto() {
        return kto;
    }

    public void setKto(final String kto) {
        this.kto = kto;
    }

    public SOLL_HABEN getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(final SOLL_HABEN transactionType) {
        this.transactionType = transactionType;
    }

    public String getGrp() {
        return grp;
    }

    public void setGrp(final String grp) {
        this.grp = grp;
    }

    public String getgKto() {
        return gKto;
    }

    public void setgKto(final String gKto) {
        this.gKto = gKto;
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

    public int getkIndx() {
        return kIndx;
    }

    public void setkIndx(final int kIndx) {
        this.kIndx = kIndx;
    }

    public int getbType() {
        return bType;
    }

    public void setbType(final int bType) {
        this.bType = bType;
    }

    public int getmType() {
        return mType;
    }

    public void setmType(final int mType) {
        this.mType = mType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public BigDecimal getNetto() {
        return netto;
    }

    public void setNetto(final BigDecimal netto) {
        this.netto = netto;
    }

    public BigDecimal getSteuer() {
        return steuer;
    }

    public void setSteuer(final BigDecimal steuer) {
        this.steuer = steuer;
    }

    public BigDecimal getFwBetrag() {
        return fwBetrag;
    }

    public void setFwBetrag(final BigDecimal fwBetrag) {
        this.fwBetrag = fwBetrag;
    }

    public String getTx1() {
        return tx1;
    }

    public void setTx1(final String tx1) {
        this.tx1 = tx1;
    }

    public String getTx2() {
        return tx2;
    }

    public void setTx2(final String tx2) {
        this.tx2 = tx2;
    }

    public int getPkKey() {
        return pkKey;
    }

    public void setPkKey(final int pkKey) {
        this.pkKey = pkKey;
    }

    public String getOpId() {
        return opId;
    }

    public void setOpId(final String opId) {
        this.opId = opId;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(final int flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return new StringJoiner(DELIMITER)//
            .add(blg) //
            .add(LocalDateConverter.toString(datum)) //
            .add(kto) //
            .add(transactionType.toString()) //
            .add(grp) //
            .add(gKto) //
            .add(sId) //
            .add(Integer.toString(sIdx)) //
            .add(Integer.toString(kIndx)).add(Integer.toString(bType)) //
            .add(Integer.toString(mType)) //
            .add(inQuotes(code)) //
            .add(BigDecimalConverter.asString(netto)) //
            .add(BigDecimalConverter.asString(steuer)) //
            .add(BigDecimalConverter.asString(fwBetrag)) //
            .add(inQuotes(tx1)) //
            .add(inQuotes(tx2)) //
            .add(Integer.toString(pkKey)) //
            .add(opId) //
            .add(Integer.toString(flag)) //
            .toString();
    }

    private String inQuotes(final String text) {
        return QUOTE_SYMBOL + text + QUOTE_SYMBOL;
    }
}
