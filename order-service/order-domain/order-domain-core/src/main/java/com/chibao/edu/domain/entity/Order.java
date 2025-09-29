package com.chibao.edu.domain.entity;

import com.chibao.edu.domain.value_object.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.util.List;

// this is aggregate root for order service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
@Builder
public class Order extends AggregateRoot<OrderId> {
    CustomerId customerId;
    RestaurantId restaurantId;
    StreetAddress deliverAddress;
    Money money;
    List<OrderItem> items;

    @NonFinal
    TrackingId trackingId;
    @NonFinal
    OrderStatus orderStatus;
    @NonFinal
    List<String> failureMessages;
}
