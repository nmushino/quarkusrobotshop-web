package io.quarkusrobotshop.web.domain;

import io.quarkusrobotshop.domain.Item;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

public class LineItem {

    Item item;

    String name;

    String orderId;

    BigDecimal price;

    public LineItem(Item item, String name, String orderId, BigDecimal price) {
        this.item = item;
        this.name = name;
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LineItem.class.getSimpleName() + "[", "]")
                .add("item=" + item)
                .add("name='" + name + "'")
                .add("orderId='" + orderId + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LineItem lineItem = (LineItem) o;

        if (item != lineItem.item) return false;
        if (!Objects.equals(name, lineItem.name)) return false;
        return Objects.equals(orderId, lineItem.orderId);
    }

    @Override
    public int hashCode() {
        int result = item != null ? item.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (orderId != null ? orderId.hashCode() : 0);
        return result;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
