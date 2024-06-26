package io.quarkusrobotshop.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

@RegisterForReflection
public class ProductItem {

    Item item;

    BigDecimal price;

    public ProductItem() {
    }

    public ProductItem(Item item, BigDecimal price) {
        this.item = item;
        this.price = price;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ProductItem.class.getSimpleName() + "[", "]")
                .add("item=" + item)
                .add("price=" + price)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductItem productItem = (ProductItem) o;
        return item == productItem.item &&
                Objects.equals(price, productItem.price);
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
