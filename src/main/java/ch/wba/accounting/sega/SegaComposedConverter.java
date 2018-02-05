package ch.wba.accounting.sega;

import ch.wba.accounting.banana.BananaTransactionDto;

import java.util.ArrayList;
import java.util.List;

public class SegaComposedConverter extends AbstractSegaConverter {

    private SegaConverter converterWithoutVat = new SegaWithoutVatShortConverter();
    private SegaConverter converterWithVat = new SegaWithVatShortConverter();

    @Override
    public List<SegaDto> toSegaTransactions(BananaTransactionDto transaction) {
        final List<SegaDto> result = new ArrayList<>();
        result.add(createFirstCompoundRecord(transaction));
        for (BananaTransactionDto bananaTransactionDto : transaction.getComposedTransactions()) {
            if (bananaTransactionDto.getAmountVat() == null) {
                result.addAll(converterWithoutVat.toSegaTransactions(bananaTransactionDto));
            } else {
                result.addAll(converterWithVat.toSegaTransactions(bananaTransactionDto));
                result.get(result.size()-2).setsIdx(result.size());
            }
        }
        for (int i=1; i<result.size(); i++) {
            result.get(i).setgKto(transaction.getCreditAccount());
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
