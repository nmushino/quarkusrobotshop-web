package io.quarkusrobotshop.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Thrown when an item is out of stock
 */
@RegisterForReflection
public class EightySixException extends Exception {

    Item item;

    public EightySixException(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public Collection<EightySixEvent> getEvents() {
        return List.of(new EightySixEvent(item));
    }
}
