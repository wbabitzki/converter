package ch.wba.accounting.sega.converter;

import ch.wba.accounting.banana.BananaTransactionDto;
import ch.wba.accounting.sega.SegaDto;

public class SegaReversalConverter extends AbstractSegaTwoRecordsConverter {

    @Override
    protected SegaDto adjustFirstRecord(SegaDto sega, BananaTransactionDto banana) {
        sega.setKto(banana.getCreditAccount());
        sega.setgKto(banana.getDebitAccount());
        sega.setNetto(sega.getNetto().negate());
        sega.setSteuer(sega.getSteuer().negate());
        return sega;
    }

    @Override
    protected SegaDto adjustSecondRecord(SegaDto sega, BananaTransactionDto banana) {
        sega.setKto(banana.getCreditAccount());
        sega.setgKto(banana.getVatAccount());
        sega.setNetto(sega.getNetto().negate());
        sega.setSteuer(sega.getSteuer().negate());
        return sega;
    }
}
