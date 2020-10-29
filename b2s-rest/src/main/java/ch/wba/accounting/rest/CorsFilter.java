package ch.wba.accounting.rest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class CorsFilter implements ContainerResponseFilter {
    @Override
    public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext) {
        responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
        responseContext.getHeaders().add("Access-Control-Allow-Methods", "*");
        responseContext.getHeaders().add("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization, Error-Reason");
        responseContext.getHeaders().add("Access-Control-Expose-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization, Error-Reason");
    }
}
