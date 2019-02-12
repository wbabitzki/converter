package ch.wba.accounting.producer;

import ch.wba.accounting.banana.BananaTransactionReader;
import ch.wba.accounting.sega.ConverterService;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import java.io.Serializable;

@SessionScoped
public class BeanProducer implements Serializable {
    private static final long serialVersionUID = 1678330689973033351L;

    @Produces
    public BananaTransactionReader getBananaReader() {
        return new BananaTransactionReader();
    }

    @Produces
    public ConverterService getConverterServicer() {
        return new ConverterService();
    }
}
