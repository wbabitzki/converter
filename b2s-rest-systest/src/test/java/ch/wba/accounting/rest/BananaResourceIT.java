package ch.wba.accounting.rest;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.*;
import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

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

public class BananaResourceIT {
    private WebTarget tut;
    static final String SERVICE_URI = "http://localhost:8080/b2s-rest/rest/banana/";

    @Before
    public void init() {
        this.tut = ClientBuilder.newClient().register(MultiPartFeature.class).target(SERVICE_URI);
    }

    @Test
    public void readFile_csvFile_readsAllRecords() throws Exception {
        // arrange
        final File file = new File(getClass().getClassLoader().getResource("test-banana.csv").getFile());
        Response response = null;
        final FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file", file);
        // act
        try (final FormDataMultiPart formDataMultiPart = new FormDataMultiPart()) {
            final MultiPart multiPart = formDataMultiPart.bodyPart(fileDataBodyPart);
            response = this.tut //
                .path("readFile") //
                .request(MediaType.APPLICATION_JSON) //
                .post(Entity.entity(multiPart, multiPart.getMediaType()));
        }
        // assert
        assertThat(response.getStatus(), is(200));
        final List<BananaTransactionDto> result = response.readEntity(jsonConverter());
        assertThat(result, hasSize(11));
    }

    private GenericType<List<BananaTransactionDto>> jsonConverter() {
        return new GenericType<List<BananaTransactionDto>>() {
            // Empty
        };
    }
}
