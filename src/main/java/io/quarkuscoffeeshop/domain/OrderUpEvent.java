package io.quarkusrobotshop.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.Objects;
import java.util.StringJoiner;

@RegisterForReflection
public class OrderUpEvent extends LineItemEvent{

    public String madeBy;

    public OrderUpEvent() {
        super();
    }

    public OrderUpEvent(EventType eventType, String orderId, String name, Item item, String itemId, String madeBy) {
        super(eventType, orderId, name, item, itemId);
        this.madeBy = madeBy;
    }

    @Override
    public EventType getEventType() {
        return this.eventType;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", OrderUpEvent.class.getSimpleName() + "[", "]")
                .add("madeBy='" + madeBy + "'")
                .add("itemId='" + itemId + "'")
                .add("orderId='" + orderId + "'")
                .add("eventType=" + eventType)
                .add("name='" + name + "'")
                .add("item=" + item)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderUpEvent that = (OrderUpEvent) o;
        return Objects.equals(madeBy, that.madeBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(madeBy);
    }

    public String getMadeBy() {
        return madeBy;
    }

    public void setMadeBy(String madeBy) {
        this.madeBy = madeBy;
    }
}
