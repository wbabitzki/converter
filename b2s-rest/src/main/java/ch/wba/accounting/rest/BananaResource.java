package ch.wba.accounting.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataParam;

import ch.wba.accounting.banana.BananaTransactionDto;
import ch.wba.accounting.banana.BananaTransactionReader;

@Stateless
@Path("banana")
public class BananaResource {
    private BananaTransactionReader bananaReader;

    @Inject
    public void setBananaReader(final BananaTransactionReader bananaReader) {
        this.bananaReader = bananaReader;
    }

    @Path("/readFile")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public List<BananaTransactionDto> readFile(@FormDataParam("file") final InputStream inputStream) {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
            return bananaReader.readTransactions(reader);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
