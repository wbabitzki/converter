package ch.wba.accounting.producer;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;

import ch.wba.accounting.banana.BananaTransactionReader;

@SessionScoped
public class BeanProducer implements Serializable {
    private static final long serialVersionUID = 1678330689973033351L;

    @Produces
    public BananaTransactionReader getBananaReader() {
        return new BananaTransactionReader();
    }
}
