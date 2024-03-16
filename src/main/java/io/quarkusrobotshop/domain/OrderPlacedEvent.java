package io.quarkusrobotshop.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@RegisterForReflection
public class OrderPlacedEvent {

    public String id;

    OrderSource orderSource;

    public String rewardsId;

    public List<LineItem> homerobots = new ArrayList<>();

    public List<LineItem> prorobotOrders = new ArrayList<>();

    public final EventType eventType = EventType.ORDER_PLACED;

    public OrderPlacedEvent() {
    }

    public List<LineItem> getBeverages() {
        return homerobots == null ? new ArrayList<>() : homerobots;
    }

    public List<LineItem> getProrobotOrders() {
        return prorobotOrders == null ? new ArrayList<>() : prorobotOrders;
    }

    public void addBeverages(final String id, final List<LineItem> homerobotList) {
        this.id = id;
        this.homerobots.addAll(homerobotList);
    }

    public void addProrobotItems(final String id, final List<LineItem> prorobotOrdersList) {
        this.id = id;
        this.prorobotOrders.addAll(prorobotOrdersList);
    }

    public OrderPlacedEvent(String id, OrderSource orderSource, String rewardsId, List<LineItem> homerobots, List<LineItem> prorobotOrders) {
        this.id = id;
        this.orderSource = orderSource;
        this.rewardsId = rewardsId;
        this.homerobots = homerobots;
        this.prorobotOrders = prorobotOrders;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", OrderPlacedEvent.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("orderSource=" + orderSource)
                .add("rewardsId=" + rewardsId)
                .add("homerobots=" + homerobots)
                .add("prorobotOrders=" + prorobotOrders)
                .add("eventType=" + eventType)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderPlacedEvent that = (OrderPlacedEvent) o;
        return Objects.equals(id, that.id) &&
                orderSource == that.orderSource &&
                rewardsId == that.rewardsId &&
                Objects.equals(homerobots, that.homerobots) &&
                Objects.equals(prorobotOrders, that.prorobotOrders) &&
                eventType == that.eventType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderSource, rewardsId, homerobots, prorobotOrders, eventType);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OrderSource getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(OrderSource orderSource) {
        this.orderSource = orderSource;
    }

    public String getRewardsId() {
        return rewardsId;
    }

    public void setRewardsId(String rewardsId) { this.rewardsId = rewardsId; }

    public void setBeverages(List<LineItem> homerobots) {
        this.homerobots = homerobots;
    }

    public void setProrobotOrders(List<LineItem> prorobotOrders) {
        this.prorobotOrders = prorobotOrders;
    }
}
