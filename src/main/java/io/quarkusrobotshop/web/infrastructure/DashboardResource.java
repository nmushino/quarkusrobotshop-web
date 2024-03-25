package io.quarkusrobotshop.web.infrastructure;

import io.quarkusrobotshop.web.domain.DashboardUpdate;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.Publisher;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;

@RegisterForReflection
@Path("/dashboard")
@SseElementType("text/event-stream")
public class DashboardResource {

    Logger logger = Logger.getLogger(DashboardResource.class);

    private Sse sse;
    private static SseBroadcaster broadcaster;

    @Inject
    @Channel("web-updates")
    @Broadcast
    Publisher<DashboardUpdate> updater;

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS) // denotes that server side events (SSE) will be produced
    //@SseElementType("text/plain") // denotes that the contained data, within this SSE, is just regular text/plain data
    @SseElementType("text/event-stream")
    public Publisher<DashboardUpdate> dashboardStream(@Context SseEventSink eventSink, @Context Sse sse) {

        this.sse = sse;
        SseBroadcaster broadcaster = getBroadcaster(sse);       
        broadcaster.register(eventSink);

        return updater;
    }

    // Bug 回避
    private synchronized static SseBroadcaster getBroadcaster(Sse sse) {
        
        if (broadcaster == null) {
            broadcaster = sse.newBroadcaster();
        } else {
            RestResource rest = new RestResource();
            rest.broadcast("message", sse, broadcaster);
        }
        return broadcaster;
    }
}
