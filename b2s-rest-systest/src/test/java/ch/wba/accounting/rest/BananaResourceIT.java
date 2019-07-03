package ch.wba.accounting.rest;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.junit.Before;
import org.junit.Test;

import ch.wba.accounting.banana.BananaTransactionDto;
import ch.wba.accounting.banana.validation.BananaViolation;
import ch.wba.accounting.sega.SegaDto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class BananaResourceIT {
    static final String SERVICE_URI = "http://localhost:8080/b2s-rest/rest/banana/";
    static final String PATH_READ_FILE = "readFile";
    static final String PATH_CONVERT = "convert";
    static final String PATH_VALIDATE = "validate";
    static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("d.M.yyyy");
    private static final String TEST_BANANA_JSON = "test-banana.json";
    private static final UUID TEST_UUID = UUID.fromString("34844b03-d68d-45f6-93b4-b07701f7a016");
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
        objectMapper.registerModule(new SimpleModule().addDeserializer(BananaViolation.class, new JsonDeserializer<BananaViolation>() {
            @Override
            public BananaViolation deserialize(final JsonParser jsonParser, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
                final JsonNode node = jsonParser.getCodec().readTree(jsonParser);
                return new BananaViolation( //
                    UUID.fromString(((TextNode) node.get("uuid")).asText()), //
                    ((TextNode) node.get("field")).asText(), //
                    ((TextNode) node.get("message")).asText());
            }
        }));
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
        final String jsonString = readTestFile(TEST_BANANA_JSON);
        //act
        final Response response = this.tut //
            .path(PATH_CONVERT) //
            .request(MediaType.APPLICATION_JSON) //
            .post(Entity.json(jsonString));
        //assert
        assertThat(response.getStatus(), is(200));
        final List<SegaDto> result = objectMapper.readValue(response.readEntity(String.class), new TypeReference<List<SegaDto>>() {
            // Empty
        });
        assertThat(result, hasSize(30));
    }

    @Test
    public void validate_validListBananaDto_createsJsonEmtyList() throws Exception {
        //arrange
        final String jsonString = readTestFile(TEST_BANANA_JSON);
        //act
        final Response response = this.tut //
            .path(PATH_VALIDATE) //
            .request(MediaType.APPLICATION_JSON) //
            .post(Entity.json(jsonString));
        //assert
        assertThat(response.getStatus(), is(200));
        final Map<UUID, List<BananaViolation>> result = response.readEntity(new GenericType<Map<UUID, List<BananaViolation>>>() {
            // Empty
        });
        assertTrue(result.isEmpty());
    }

    @Test
    public void validate_invalidListBananaDto_createsBananaViolationMap() throws Exception {
        //arrange
        final String jsonString = readTestFile("test-invalid-banana.json");
        //act
        final Response response = this.tut //
            .path(PATH_VALIDATE) //
            .request(MediaType.APPLICATION_JSON) //
            .post(Entity.json(jsonString));
        //assert
        assertThat(response.getStatus(), is(200));
        final Map<UUID, List<BananaViolation>> result = objectMapper.readValue(response.readEntity(String.class), new TypeReference<Map<UUID, List<BananaViolation>>>() {
            // Empty
        });
        assertThat(result.get(TEST_UUID).get(0).getField(), is("fromAccount"));
    }

    private String readTestFile(final String fileName) throws Exception {
        final URI uri = getClass().getClassLoader().getResource(fileName).toURI();
        return new String(Files.readAllBytes(Paths.get(uri)), Charset.forName("UTF-8"));
    }
}
