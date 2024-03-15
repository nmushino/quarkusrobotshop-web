package io.quarkusrobotshop.web.infrastructure;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkusrobotshop.domain.valueobjects.LoyaltyUpdate;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.Publisher;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RegisterForReflection
@Path("/dashboard")
public class LoyaltyDashboard {

    Logger logger = Logger.getLogger(LoyaltyDashboard.class);

    @Inject
    @Channel("loyalty-updates")
    @Broadcast
    Publisher<LoyaltyUpdate> updater;

    @GET
    @Path("/loyaltystream")
    @Produces(MediaType.SERVER_SENT_EVENTS) // denotes that server side events (SSE) will be produced
    @SseElementType("text/plain") // denotes that the contained data, within this SSE, is just regular text/plain data
    public Publisher<LoyaltyUpdate> dashboardStream() {

        return updater;
    }
}
