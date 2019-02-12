package ch.wba.accounting.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.ws.rs.ext.ContextResolver;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class JacksonObjectMapperProvider implements ContextResolver<ObjectMapper> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final ObjectMapper defaultObjectMapper;

    public JacksonObjectMapperProvider() {
        defaultObjectMapper = createDefaultMapper();
    }

    @Override
    public ObjectMapper getContext(final Class<?> type) {
        return defaultObjectMapper;
    }

    private static ObjectMapper createDefaultMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        final JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule //
        .addSerializer(LocalDate.class, new JsonSerializer<LocalDate>() {
            @Override
            public void serialize(final LocalDate value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
                gen.writeString(value.format(FORMATTER));
            }
        }) //
        .addDeserializer(LocalDate.class, new JsonDeserializer<LocalDate>() {
            @Override
            public LocalDate deserialize(final JsonParser p, final DeserializationContext ctx) throws IOException {
                try {
                    return LocalDate.parse(p.getValueAsString(), FORMATTER);
                } catch (final DateTimeParseException e) {
                    System.out.format("Invalid date: '%s', not confirm to the format '%s'", p.getValueAsString(), FORMATTER);
                    return null;
                }
            }
        });
        mapper.registerModule(javaTimeModule);
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        return mapper;
    }
}
