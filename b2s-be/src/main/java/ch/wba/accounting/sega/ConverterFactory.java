package ch.wba.accounting.sega;

import ch.wba.accounting.banana.BananaTransactionDto;
import ch.wba.accounting.sega.converter.SegaConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class ConverterFactory {

    private final Map<Predicate<BananaTransactionDto>, SegaConverter> converters = new HashMap<>();

    public void register(final Predicate<BananaTransactionDto> condition, final SegaConverter converter) {
        converters.put(condition, converter);
    }

    public SegaConverter create(final BananaTransactionDto transaction) {
        return converters //
                .entrySet() //
                .stream() //
                .filter(e -> e.getKey().test(transaction)) //
                .map(Map.Entry::getValue) //
                .findFirst() //
                .orElse(null);
    }
}
