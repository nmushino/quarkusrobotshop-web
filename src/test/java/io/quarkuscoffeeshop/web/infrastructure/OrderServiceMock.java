package io.quarkusrobotshop.web.infrastructure;

import io.quarkus.test.Mock;
import io.quarkusrobotshop.web.domain.commands.PlaceOrderCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import java.util.concurrent.CompletableFuture;


//@Mock
@Alternative
@ApplicationScoped
public class OrderServiceMock extends OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceMock.class);

    @Override
    public CompletableFuture<Void> placeOrder(final PlaceOrderCommand placeOrderCommand){

        logger.info("onOrderIn called on Mock: {}", placeOrderCommand);
        return new CompletableFuture<>();
    }

}