package ch.wba.accounting.sega;

import ch.wba.accounting.banana.BananaTransactionDto;
import ch.wba.accounting.sega.converter.SegaConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class ConverterFactory {

    private final Map<Predicate<BananaTransactionDto>, SegaConverter> converters = new HashMap<>();

    public SegaConverter register(final Predicate<BananaTransactionDto> condition, final SegaConverter converter) {
        return converters.put(condition, converter);
    }

    public SegaConverter create(final BananaTransactionDto transaction) {
        final Optional<SegaConverter> converter = converters //
                .entrySet() //
                .stream() //
                .filter(e -> e.getKey().test(transaction)) //
                .map(Map.Entry::getValue) //
                .findFirst();
        return converter.isPresent() ? converter.get() : null;
    }
}
