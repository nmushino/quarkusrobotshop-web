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

        sendHttp(streamUrl);
        
        return orderService.placeOrder(placeOrderCommand)
            .thenApply(res -> Response.accepted().entity(placeOrderCommand).build()).exceptionally(ex -> {
                    logger.error(ex.getMessage());
                    return Response.serverError().entity(ex).build();
            });
    }
    
    @POST
	public void broadcast(String message, Sse sse, SseBroadcaster broadcaster) {
        
        if (placeordercommander != null) {

            String homerobotitem = placeordercommander.getHomerobotItems().toString();
            String prorobotitem = placeordercommander.getProrobotItems().toString();
            String state = placeordercommander.toString();

            jsonListItem infoHomerobot = new jsonListItem();
            jsonListItem infoProrobot = new jsonListItem();
            jsonListItem info = new jsonListItem();

            try {

            // Bug 回避
            String states = readState(state);
            OutboundSseEvent event = null;

            if ( !homerobotitem.equals("Optional[[]]")) {
                String[] homerobot = readNode(homerobotitem);

                Random rand = new Random();
                int num = rand.nextInt(100);
                
                infoHomerobot.name = homerobot[2];
                infoHomerobot.item = homerobot[0];
                infoHomerobot.status = states;
                infoHomerobot.madeBy = num;
            }
            if ( !prorobotitem.equals("Optional[[]]")) {
                String[] prorobot = readNode(prorobotitem);

                Random rand = new Random();
                int num = rand.nextInt(1000);
                
                infoProrobot.name = prorobot[2];
                infoProrobot.item = prorobot[0];
                infoProrobot.status = states;
                infoProrobot.madeBy = num;
            }
            
            if (!homerobotitem.equals("Optional[[]]")) {
                info = infoHomerobot;
            } else if (!prorobotitem.equals("Optional[[]]")) {
                info = infoProrobot;
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
