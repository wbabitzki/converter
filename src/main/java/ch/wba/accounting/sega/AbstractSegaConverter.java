package ch.wba.accounting.sega;

import ch.wba.accounting.banana.BananaTransactionDto;

import java.math.BigDecimal;

public abstract class AbstractSegaConverter implements SegaConverter {

    protected SegaDto createDefaultSegaDto(BananaTransactionDto transaction) {
        SegaDto defaultDto = new SegaDto();

        defaultDto.setBlg(transaction.getDocument());
        defaultDto.setGrp(" ");
        defaultDto.setDatum(transaction.getDate());
        defaultDto.setsId("");
        defaultDto.setCode("");
        defaultDto.setSteuer(BigDecimal.ZERO);
        defaultDto.setFwBetrag(BigDecimal.ZERO);
        defaultDto.setTx1(transaction.getDescription());
        defaultDto.setOpId("");
        
        return defaultDto;
    }
}
