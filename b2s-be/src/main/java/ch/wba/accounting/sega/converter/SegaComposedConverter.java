package ch.wba.accounting.sega.converter;

import ch.wba.accounting.banana.BananaTransactionDto;
import ch.wba.accounting.sega.SegaDto;

import java.util.ArrayList;
import java.util.List;

public class SegaComposedConverter extends AbstractSegaConverter {

    final private SegaConverter converterWithoutVat = new SegaWithoutVatShortConverter();
    final private SegaConverter converterWithVat = new SegaWithVatShortConverter();
    final private SegaConverter converterReversalWithVat = new SegaReversalConverter();

    @Override
    public List<SegaDto> toSegaTransactions(BananaTransactionDto transaction) {
        final List<SegaDto> result = new ArrayList<>();
        result.add(createFirstCompoundRecord(transaction));
        for (BananaTransactionDto bananaTransactionDto : transaction.getIntegratedTransactions()) {
            if (bananaTransactionDto.getAmountVat() == null) {
                result.addAll(converterWithoutVat.toSegaTransactions(bananaTransactionDto));
            } else {
                if (bananaTransactionDto.isReversal()) {
                    result.addAll(converterReversalWithVat.toSegaTransactions(bananaTransactionDto));
                } else {
                    result.addAll(converterWithVat.toSegaTransactions(bananaTransactionDto));
                }
                result.get(result.size() - 2).setsIdx(result.size());
            }
        }
        for (int i=1; i<result.size(); i++) {
            final SegaDto dto = result.get(i);
            if (dto.getKto() == null || dto.getKto().length() == 0) {
                dto.setKto(transaction.getCreditAccount());
            } else {
                dto.setgKto(transaction.getCreditAccount());
            }
        }
        return result;
    }

    private SegaDto createFirstCompoundRecord(final BananaTransactionDto transaction) {
        final SegaDto segaDto = createDefaultSegaDto(transaction);
        segaDto.setKto(transaction.getCreditAccount());
        segaDto.setTransactionType(SegaDto.SOLL_HABEN.HABEN);
        segaDto.setgKto("div");
        segaDto.setNetto(transaction.getAmount());
        segaDto.setmType(2);
        segaDto.setTx2("");
        return segaDto;
    }
}
