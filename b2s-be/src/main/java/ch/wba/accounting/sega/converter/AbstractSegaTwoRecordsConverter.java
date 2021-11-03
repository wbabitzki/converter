package ch.wba.accounting.sega.converter;

import ch.wba.accounting.banana.BananaTransactionDto;
import ch.wba.accounting.sega.SegaDto;

import java.util.Arrays;
import java.util.List;

abstract class AbstractSegaTwoRecordsConverter extends AbstractSegaConverter {
    @Override
    final public List<SegaDto> toSegaTransactions(final BananaTransactionDto transaction) {
        final SegaDto first = createFirstTransaction(transaction);
        final SegaDto tax = createSecondTransaction(transaction);
        return Arrays.asList(
                adjustFirstRecord(first, transaction),
                adjustSecondRecord(tax, transaction));
    }

    private SegaDto createFirstTransaction(final BananaTransactionDto transaction) {
        final SegaDto sega = createDefaultSegaDto(transaction);
        sega.setTransactionType(SegaDto.SOLL_HABEN.SOLL);
        sega.setsId(transaction.getVatCode());
        sega.setNetto(transaction.getAmountWithoutVat());
        sega.setSteuer(transaction.getAmountVat());
        sega.setmType(2);
        sega.setTx2("");
        return sega;
    }

    private SegaDto createSecondTransaction(final BananaTransactionDto transaction) {
        final SegaDto sega = createDefaultSegaDto(transaction);
        sega.setTransactionType(SegaDto.SOLL_HABEN.SOLL);
        sega.setNetto(transaction.getAmountVat());
        sega.setSteuer(transaction.getAmountWithoutVat());
        sega.setmType(2);
        sega.setbType(2);
        sega.setTx2(transaction.getVatPct().abs() + "%");
        return sega;
    }

    abstract protected SegaDto adjustFirstRecord(SegaDto sega, BananaTransactionDto accountTransaction);

    abstract protected SegaDto adjustSecondRecord(SegaDto sega, BananaTransactionDto accountTransaction);
}
