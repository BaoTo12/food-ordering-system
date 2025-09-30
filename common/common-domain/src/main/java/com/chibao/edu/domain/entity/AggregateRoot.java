package com.chibao.edu.domain.entity;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public abstract class AggregateRoot<ID> extends BaseEntity<ID> {
    protected AggregateRoot() {
    }
}
