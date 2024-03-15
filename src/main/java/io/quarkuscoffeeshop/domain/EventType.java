package io.quarkusrobotshop.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public enum EventType {
    BEVERAGE_ORDER_IN, BEVERAGE_ORDER_UP, EIGHTY_SIX, PROBOT_ORDER_IN, PROBOT_ORDER_UP, ORDER_PLACED, RESTOCK, NEW_ORDER
}
