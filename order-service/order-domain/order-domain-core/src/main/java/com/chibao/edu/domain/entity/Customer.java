package com.chibao.edu.domain.entity;

import com.chibao.edu.domain.value_object.CustomerId;

public class Customer extends AggregateRoot<CustomerId>{
    protected Customer(AggregateRootBuilder<CustomerId, ?, ?> b) {
        super(b);
    }
}
