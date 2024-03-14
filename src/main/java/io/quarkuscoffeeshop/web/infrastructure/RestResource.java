package io.quarkuscoffeeshop.web.infrastructure;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkuscoffeeshop.web.domain.commands.PlaceOrderCommand;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;

import java.util.Random;
import java.util.concurrent.CompletionStage;
import static io.quarkuscoffeeshop.web.infrastructure.JsonUtil.toJson;
import static io.quarkuscoffeeshop.web.infrastructure.JsonUtil.readNode;
import static io.quarkuscoffeeshop.web.infrastructure.JsonUtil.readState;
import static io.quarkuscoffeeshop.web.infrastructure.HttpUtil.sendHttp;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;

@Singleton
@RegisterForReflection
@Path("/")
public class RestResource {

    Logger logger = LoggerFactory.getLogger(RestResource.class);

    private Sse sse;
    private static SseBroadcaster broadcaster;
    private static PlaceOrderCommand placeordercommander;

    @ConfigProperty(name="streamUrl")
    String streamUrl;

    @ConfigProperty(name="loyaltyStreamUrl")
    String loyaltyStreamUrl;

    @ConfigProperty(name="storeId")
    String storeId;

    @Inject
    OrderService orderService;

    @Inject
    Template cafeTemplate;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getIndex(){
        System.out.println("AAAAAAAAAAAAA  "+streamUrl);

        return cafeTemplate
                .data("streamUrl", streamUrl)
                .data("loyaltyStreamUrl", loyaltyStreamUrl)
                .data("storeId", storeId);
    }

    // 画面出力用Class
    static class jsonListItem {
        public String name = null;
        public String item = null;
        public String status = null;
        public int madeBy = 0;
    }

    @Context
	public synchronized void setSse(Sse sse) { 
		if (this.sse != null) {
			return;
		}
		this.sse = sse;
		this.broadcaster = sse.newBroadcaster();
		this.broadcaster.onClose(eventSink -> logger.info("On close EventSink: {}", eventSink));
		this.broadcaster.onError(
				(eventSink, throwable) -> logger.info("On Error EventSink: {}, Throwable: {}", eventSink, throwable));
	}

    @POST
    @Path("order")
    public CompletionStage<Response> orderIn(final PlaceOrderCommand placeOrderCommand) {

        logger.debug("order received: {}", placeOrderCommand.toString());
        
        this.sse = sse;
        this.broadcaster = sse.newBroadcaster();
        placeordercommander = placeOrderCommand;
        
        return orderService.placeOrder(placeOrderCommand)
            .thenApply(res -> Response.accepted().entity(placeOrderCommand).build()).exceptionally(ex -> {
                    logger.error(ex.getMessage());
                    return Response.serverError().entity(ex).build();
            });
    }
    
    @POST
	public void broadcast(String message, Sse sse, SseBroadcaster broadcaster) {
        
        if (placeordercommander != null) {

            String baristaitem = placeordercommander.getBaristaItems().toString();
            String kitchenitem = placeordercommander.getKitchenItems().toString();
            String state = placeordercommander.toString();

            jsonListItem infoBarista = new jsonListItem();
            jsonListItem infoKitchen = new jsonListItem();
            jsonListItem info = new jsonListItem();

            try {

            // Bug 回避
            String states = readState(state);
            OutboundSseEvent event = null;

            if ( !baristaitem.equals("Optional[[]]")) {
                String[] barista = readNode(baristaitem);

                Random rand = new Random();
                int num = rand.nextInt(20);
                
                infoBarista.name = barista[2];
                infoBarista.item = barista[0];
                infoBarista.status = states;
                infoBarista.madeBy = num;
            }
            if ( !kitchenitem.equals("Optional[[]]")) {
                String[] kitchen = readNode(kitchenitem);

                Random rand = new Random();
                int num = rand.nextInt(40);
                
                infoKitchen.name = kitchen[2];
                infoKitchen.item = kitchen[0];
                infoKitchen.status = states;
                infoKitchen.madeBy = num;
            }
            
            if (!baristaitem.equals("Optional[[]]")) {
                info = infoBarista;
            } else if (!kitchenitem.equals("Optional[[]]")) {
                info = infoKitchen;
            }

            // 断定のイベント送信
            event = sse.newEventBuilder()
            .name(message)
            .reconnectDelay(100000)
            .mediaType(MediaType.APPLICATION_JSON_TYPE)
            .data(toJson(info))
            .build();

            try {
                Thread.sleep(3000);
            }
            catch (InterruptedException e) {}

            broadcaster.broadcast(event);

            } catch (JsonParseException | JsonMappingException ex) {
                logger.error(ex.getMessage());
            } catch (IOException ex) {
                logger.error(ex.getMessage());
            }

        }
	}
}
