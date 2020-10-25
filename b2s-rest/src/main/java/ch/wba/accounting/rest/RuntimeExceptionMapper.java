package ch.wba.accounting.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RuntimeExceptionMapper implements ExceptionMapper <RuntimeException> {

    @Override
    public Response toResponse(RuntimeException exception) {
        return Response.status(500).header("Error-Reason", exception.getMessage()).build();
    }
}
