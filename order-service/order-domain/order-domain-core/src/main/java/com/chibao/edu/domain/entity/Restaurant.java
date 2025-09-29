package com.chibao.edu.domain.entity;

import com.chibao.edu.domain.value_object.RestaurantId;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class Restaurant extends AggregateRoot<RestaurantId> {
    private final List<Product> products;
    private boolean active;

}
