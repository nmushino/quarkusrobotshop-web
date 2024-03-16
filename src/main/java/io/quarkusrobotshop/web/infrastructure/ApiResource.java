package io.quarkusrobotshop.web.infrastructure;

import io.quarkusrobotshop.domain.*;
import io.quarkusrobotshop.web.domain.DashboardUpdate;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkusrobotshop.web.domain.commands.PlaceOrderCommand;
import org.jboss.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

@RegisterForReflection
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class ApiResource {

    Logger logger = Logger.getLogger(ApiResource.class);

    @GET
    @Path("/update")
    public Response getCreateOrderCommandJson() {
        DashboardUpdate dashboardUpdate = new DashboardUpdate(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "Noriaki",
                Item.FAC94S3,
                OrderStatus.IN_QUEUE,
                null);
        return Response.ok().entity(dashboardUpdate).build();
    }

    @GET
    @Path("/placeOrderCommand")
    public Response getPlaceOrderCommand() {

        PlaceOrderCommand placeOrderCommand = new PlaceOrderCommand(
                UUID.randomUUID().toString(),
                "TOKYO",
                OrderSource.WEB,
                null,
                Collections.singletonList(new OrderLineItem(Item.CH99A9, BigDecimal.valueOf(755000), "Goofy")),
                Collections.singletonList(new OrderLineItem(Item.FAC94S3, BigDecimal.valueOf(2.99), "Goofy")),
                BigDecimal.valueOf(5.98)
        );

        return Response.ok().entity(placeOrderCommand).build();
    }
}