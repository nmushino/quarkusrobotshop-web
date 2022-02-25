package io.quarkuscoffeeshop.web.infrastructure;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkuscoffeeshop.domain.Item;
import io.quarkuscoffeeshop.domain.OrderStatus;
import io.quarkuscoffeeshop.web.domain.DashboardUpdate;
import io.smallrye.reactive.messaging.connectors.InMemoryConnector;
import io.smallrye.reactive.messaging.connectors.InMemorySource;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.InboundSseEvent;
import javax.ws.rs.sse.SseEventSource;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@QuarkusTest
public class SSETest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SSETest.class);

    private static final String expectedPayload = "data: {\"orderId\":\"82124c69-a108-4ccc-9ac4-64566e389178\",\"itemId\":\"f84cb5e2-a3fd-43af-8df8-b5d74b133115\",\"name\":\"Scotty\",\"item\":\"COFFEE_WITH_ROOM\",\"status\":\"IN_QUEUE\",\"madeBy\":\"\"}";

    @Inject
    @Any
    InMemoryConnector connector;

    @Test
    public void testSSE() throws IOException, InterruptedException, URISyntaxException {

        InMemoryConnector.switchIncomingChannelsToInMemory("web-updates");

        DashboardUpdate dashboardUpdate = new DashboardUpdate(
                "82124c69-a108-4ccc-9ac4-64566e389178",
                "f84cb5e2-a3fd-43af-8df8-b5d74b133115",
                "Scotty",
                Item.COFFEE_WITH_ROOM,
                OrderStatus.IN_QUEUE,
                null
        );

        LOGGER.info("{}", dashboardUpdate);

        URI uri = new URI("http://localhost:8080/dashboard/stream");
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(uri);

        SseEventSource sseEventSource = SseEventSource.target(target).build();
        sseEventSource.register(onEvent, onError, onComplete);
        sseEventSource.open();

        // marshall the JSON payload
        String formattedUpdate = JsonUtil.toJson(dashboardUpdate);

        // send the JSON payload
        InMemorySource<String> source = connector.source("web-updates");
        source.send(formattedUpdate);
        LOGGER.info("updates sent {}", formattedUpdate);

    }

    // A new event is received
    private static Consumer<InboundSseEvent> onEvent = (inboundSseEvent) -> {
        String data = inboundSseEvent.readData();
        LOGGER.info("event received: {}", data);
        assertEquals(expectedPayload, data);
    };

    //Error
    private static Consumer<Throwable> onError = (throwable) -> {
        LOGGER.error("error {}", throwable.getMessage());
        assertNull(throwable);
        throwable.printStackTrace();
    };

    //Connection close and there is nothing to receive
    private static Runnable onComplete = () -> {
        LOGGER.info("done!");
    };
}
