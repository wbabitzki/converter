package ch.wba.accounting.rest;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import ch.wba.accounting.converters.BigDecimalConverter;
import ch.wba.accounting.converters.LocalDateConverter;
import ch.wba.accounting.sega.SegaDto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class DefaultObjectMapper extends ObjectMapper {
    private static final long serialVersionUID = -7381800116218617750L;
    private static final String ZERO_FORMATTED = BigDecimalConverter.asString(BigDecimal.ZERO);

    protected DefaultObjectMapper() {
        super();
        initDefaultMapper();
    }

    private SimpleModule createSegaDtoModule() {
        final SimpleModule segaDtoModule = new SimpleModule();
        segaDtoModule.addSerializer(SegaDto.class, new JsonSerializer<SegaDto>() {
            @Override
            public void serialize(final SegaDto value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
                if (value != null) {
                    gen.writeStartObject();
                    gen.writeStringField("Blg", value.getBlg());
                    gen.writeStringField("Datum", value.getDatum() != null ? LocalDateConverter.toString(value.getDatum()) : "");
                    gen.writeStringField("Kto", value.getKto());
                    gen.writeStringField("S/H", value.getTransactionType() != null ? value.getTransactionType().toString() : "");
                    gen.writeStringField("Grp", value.getGrp());
                    gen.writeStringField("GKto", value.getgKto());
                    gen.writeStringField("SId", value.getsId());
                    gen.writeStringField("SIdx", Integer.toString(value.getsIdx()));
                    gen.writeStringField("KIdx", Integer.toString(value.getkIndx()));
                    gen.writeStringField("BTyp", Integer.toString(value.getbType()));
                    gen.writeStringField("MTyp", Integer.toString(value.getmType()));
                    gen.writeStringField("Code", "\"" + value.getCode() + "\"");
                    gen.writeStringField("Netto", value.getNetto() != null ? BigDecimalConverter.asString(value.getNetto()) : ZERO_FORMATTED);
                    gen.writeStringField("Steuer", value.getSteuer() != null ? BigDecimalConverter.asString(value.getSteuer()) : ZERO_FORMATTED);
                    gen.writeStringField("FW-Betrag", value.getFwBetrag() != null ? BigDecimalConverter.asString(value.getFwBetrag()) : ZERO_FORMATTED);
                    gen.writeStringField("Tx1", "\"" + value.getTx1() + "\"");
                    gen.writeStringField("Tx2", "\"" + value.getTx2() + "\"");
                    gen.writeStringField("PkKey", Integer.toString(value.getPkKey()));
                    gen.writeStringField("OpId", value.getOpId());
                    gen.writeStringField("Flag", Integer.toString(value.getFlag()));
                    gen.writeEndObject();
                }
            }
        });
        return segaDtoModule;
    }

    private JavaTimeModule createDateTimeModule() {
        final JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule //
            .addSerializer(LocalDate.class, new JsonSerializer<LocalDate>() {
                @Override
                public void serialize(final LocalDate value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
                    gen.writeString(LocalDateConverter.toString(value));
                }
            }) //
            .addDeserializer(LocalDate.class, new JsonDeserializer<LocalDate>() {
                @Override
                public LocalDate deserialize(final JsonParser p, final DeserializationContext ctx) throws IOException {
                    try {
                        return LocalDateConverter.toDate(p.getValueAsString());
                    } catch (final DateTimeParseException e) {
                        System.out.format("Invalid date: '%s'", p.getValueAsString());
                        return null;
                    }
                }
            });
        return javaTimeModule;
    }

    private void initDefaultMapper() {
        registerModule(createDateTimeModule());
        registerModule(createSegaDtoModule());
        enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    }
}
