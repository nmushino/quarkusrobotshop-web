package io.quarkuscoffeeshop.web.domain.commands;

import io.quarkuscoffeeshop.domain.OrderLineItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class WebOrderCommand {

    private final String id;

    private final String orderSource = "WEB";

    private final String location;

    private final String loyaltyMemberId;

    private final List<OrderLineItem> homerobotLineItems;

    private final List<OrderLineItem> prorobotLineItems;

    public WebOrderCommand(final PlaceOrderCommand placeOrderCommand) {

        this.id = placeOrderCommand.getId();
        this.location = placeOrderCommand.getStoreId();

        if (placeOrderCommand.getHomerobotItems().isPresent()) {
            this.homerobotLineItems = placeOrderCommand.getHomerobotItems().get();
        }else{
            this.homerobotLineItems = new ArrayList<>(0);
        }

        if (placeOrderCommand.getProrobotItems().isPresent()) {
            this.prorobotLineItems = placeOrderCommand.getProrobotItems().get();
        }else {
            this.prorobotLineItems = new ArrayList<>(0);
        }

        if (placeOrderCommand.getRewardsId().isPresent()) {
            this.loyaltyMemberId = placeOrderCommand.getRewardsId().get();
        }else{
            this.loyaltyMemberId = null;
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", WebOrderCommand.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("orderSource='" + orderSource + "'")
                .add("location='" + location + "'")
                .add("loyaltyMemberId='" + loyaltyMemberId + "'")
                .add("homerobotLineItems=" + homerobotLineItems)
                .add("prorobotLineItems=" + prorobotLineItems)
                .toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebOrderCommand that = (WebOrderCommand) o;

        if (!Objects.equals(id, that.id)) return false;
        if (orderSource != null ? !orderSource.equals(that.orderSource) : that.orderSource != null) return false;
        if (!Objects.equals(location, that.location)) return false;
        if (!Objects.equals(loyaltyMemberId, that.loyaltyMemberId))
            return false;
        if (!Objects.equals(homerobotLineItems, that.homerobotLineItems))
            return false;
        return Objects.equals(prorobotLineItems, that.prorobotLineItems);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (orderSource != null ? orderSource.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (loyaltyMemberId != null ? loyaltyMemberId.hashCode() : 0);
        result = 31 * result + (homerobotLineItems != null ? homerobotLineItems.hashCode() : 0);
        result = 31 * result + (prorobotLineItems != null ? prorobotLineItems.hashCode() : 0);
        return result;
    }

    public String getId() {
        return id;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public String getLocation() {
        return location;
    }

    public String getLoyaltyMemberId() {
        return loyaltyMemberId;
    }

    public List<OrderLineItem> getHomerobotLineItems() {
        return homerobotLineItems;
    }

    public List<OrderLineItem> getProrobotLineItems() {
        return prorobotLineItems;
    }
}
