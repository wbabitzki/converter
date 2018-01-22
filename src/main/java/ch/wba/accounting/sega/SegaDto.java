package ch.wba.accounting.sega;

import ch.wba.accounting.converters.BigDecimalConverter;
import ch.wba.accounting.converters.LocalDateConverter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.StringJoiner;

public class SegaDto {
    enum SOLL_HABEN {
        SOLL("S"),
        HABEN("H");

        private String code;
        SOLL_HABEN(String code) {
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
    private int opId;
    private int flag;

    public String getBlg() {
        return blg;
    }

    public void setBlg(String blg) {
        this.blg = blg;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public String getKto() {
        return kto;
    }

    public void setKto(String kto) {
        this.kto = kto;
    }

    public SOLL_HABEN getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(SOLL_HABEN transactionType) {
        this.transactionType = transactionType;
    }

    public String getGrp() {
        return grp;
    }

    public void setGrp(String grp) {
        this.grp = grp;
    }

    public String getgKto() {
        return gKto;
    }

    public void setgKto(String gKto) {
        this.gKto = gKto;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public int getsIdx() {
        return sIdx;
    }

    public void setsIdx(int sIdx) {
        this.sIdx = sIdx;
    }

    public int getkIndx() {
        return kIndx;
    }

    public void setkIndx(int kIndx) {
        this.kIndx = kIndx;
    }

    public int getbType() {
        return bType;
    }

    public void setbType(int bType) {
        this.bType = bType;
    }

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getNetto() {
        return netto;
    }

    public void setNetto(BigDecimal netto) {
        this.netto = netto;
    }

    public BigDecimal getSteuer() {
        return steuer;
    }

    public void setSteuer(BigDecimal steuer) {
        this.steuer = steuer;
    }

    public BigDecimal getFwBetrag() {
        return fwBetrag;
    }

    public void setFwBetrag(BigDecimal fwBetrag) {
        this.fwBetrag = fwBetrag;
    }

    public String getTx1() {
        return tx1;
    }

    public void setTx1(String tx1) {
        this.tx1 = tx1;
    }

    public String getTx2() {
        return tx2;
    }

    public void setTx2(String tx2) {
        this.tx2 = tx2;
    }

    public int getPkKey() {
        return pkKey;
    }

    public void setPkKey(int pkKey) {
        this.pkKey = pkKey;
    }

    public int getOpId() {
        return opId;
    }

    public void setOpId(int opId) {
        this.opId = opId;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return new StringJoiner(";")
                .add(blg)
                .add(LocalDateConverter.toString(datum))
                .add(kto)
                .add(transactionType.toString())
                .add(grp)
                .add(gKto)
                .add(sId)
                .add(Integer.toString(sIdx))
                .add(Integer.toString(kIndx))
                .add(Integer.toString(bType))
                .add(Integer.toString(mType))
                .add(code)
                .add(BigDecimalConverter.asString(netto))
                .add(BigDecimalConverter.asString(steuer))
                .add(BigDecimalConverter.asString(fwBetrag))
                .add(tx1)
                .add(tx2)
                .add(Integer.toString(pkKey))
                .add(Integer.toString(opId))
                .add(Integer.toString(flag))
                .toString();
    }
}
