package ch.wba.accounting.sega.converter;

import ch.wba.accounting.banana.BananaTransactionDto;
import ch.wba.accounting.sega.SegaDto;

public class SegaWithVatShortConverter extends AbstractSegaTwoRecordsConverter {

    @Override
    protected SegaDto adjustFirstRecord(SegaDto sega, BananaTransactionDto banana) {
        sega.setKto(banana.getDebitAccount());
        sega.setgKto(banana.getCreditAccount());
        return sega;
    }

    @Override
    protected SegaDto adjustSecondRecord(SegaDto sega, BananaTransactionDto banana) {
        sega.setKto(banana.getVatAccount());
        sega.setgKto(banana.getCreditAccount());
        return sega;
    }
}
