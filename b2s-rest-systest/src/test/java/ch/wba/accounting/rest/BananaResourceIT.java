package ch.wba.accounting.rest;

import ch.wba.accounting.banana.BananaTransactionDto;
import ch.wba.accounting.banana.BananaTransactionReader;
import ch.wba.accounting.sega.SegaDto;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

public class BananaResourceIT {
    static final String SERVICE_URI = "http://localhost:8080/b2s-rest/rest/banana/";
    static final String PATH_READ_FILE = "readFile";
    static final String PATH_CONVERT = "convert";
    static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private WebTarget tut;
    private ObjectMapper objectMapper;

    @Before
    public void init() {
        tut = ClientBuilder.newClient().register(MultiPartFeature.class).target(SERVICE_URI);
        objectMapper = new ObjectMapper();
        final JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDate.class, new JsonSerializer<LocalDate>() {
            @Override
            public void serialize(final LocalDate value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException, JsonProcessingException {
                gen.writeString(value.format(FORMATTER));
            }
        });
        javaTimeModule.addDeserializer(LocalDate.class, new JsonDeserializer<LocalDate>() {
            @Override
            public LocalDate deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
                return LocalDate.parse(p.getValueAsString(), FORMATTER);
            }
        });
        objectMapper.registerModule(javaTimeModule);
        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        objectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    }

    @Test
    public void readFile_csvFile_readsAllRecords() throws Exception {
        // arrange
        final File file = new File(getClass().getClassLoader().getResource("test-banana.csv").getFile());
        final FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file", file);
        Response response = null;
        // act
        try (final FormDataMultiPart formDataMultiPart = new FormDataMultiPart()) {
            final MultiPart multiPart = formDataMultiPart.bodyPart(fileDataBodyPart);
            response = this.tut //
                .path(PATH_READ_FILE) //
                .request(MediaType.APPLICATION_JSON) //
                .post(Entity.entity(multiPart, multiPart.getMediaType()));
        }
        // assert
        assertThat(response.getStatus(), is(200));
        final List<BananaTransactionDto> result = objectMapper.readValue(response.readEntity(String.class), new TypeReference<List<BananaTransactionDto>>() {
            // Empty
        });
        assertThat(result, hasSize(11));
    }

    @Test
    public void convert_listBananaDto_createListSegaDto() throws Exception {
        //arrange
        List<BananaTransactionDto> bananaTransactions = null;
        final File file = new File(getClass().getClassLoader().getResource("test-banana.csv").getFile());
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            bananaTransactions = new BananaTransactionReader().readTransactions(reader);
        }
        Response response = null;
        //act
        response = this.tut //
            .path(PATH_CONVERT) //
            .request(MediaType.APPLICATION_JSON) //
            .post(Entity.json(bananaTransactions));
        //assert
        assertThat(response.getStatus(), is(200));
        final List<SegaDto> result = objectMapper.readValue(response.readEntity(String.class), new TypeReference<List<SegaDto>>() {
            // Empty
        });
        assertThat(result, hasSize(35));
    }
}
