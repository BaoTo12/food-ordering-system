package com.chibao.edu.domain.entity;

import com.chibao.edu.domain.value_object.RestaurantId;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class Restaurant extends AggregateRoot<RestaurantId>{
    private final List<Product> products;
    private boolean active;

}
