package io.quarkusrobotshop.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public enum OrderStatus {

    IN_QUEUE, READY
}
