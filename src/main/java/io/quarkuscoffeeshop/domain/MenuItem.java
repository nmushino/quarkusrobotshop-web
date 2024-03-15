package io.quarkusrobotshop.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

@RegisterForReflection
public class MenuItem {

    Item item;

    BigDecimal price;

    public MenuItem() {
    }

    public MenuItem(Item item, BigDecimal price) {
        this.item = item;
        this.price = price;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MenuItem.class.getSimpleName() + "[", "]")
                .add("item=" + item)
                .add("price=" + price)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem menuItem = (MenuItem) o;
        return item == menuItem.item &&
                Objects.equals(price, menuItem.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, price);
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
}
