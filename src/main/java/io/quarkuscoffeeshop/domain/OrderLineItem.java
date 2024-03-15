package io.quarkusrobotshop.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

@RegisterForReflection
public class OrderLineItem {

    Item item;

    BigDecimal price;

    String name;

    public OrderLineItem() {
    }

    public OrderLineItem(Item item, BigDecimal price, String name) {
        this.item = item;
        this.price = price;
        this.name = name;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", OrderLineItem.class.getSimpleName() + "[", "]")
                .add("item=" + item)
                .add("price=" + price)
                .add("name='" + name + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderLineItem that = (OrderLineItem) o;
        return item == that.item &&
                Objects.equals(price, that.price) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, price, name);
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
