package com.chibao.edu.domain.event;

import com.chibao.edu.domain.entity.Order;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class OrderPaidEvent extends OrderEvent {
    public OrderPaidEvent(Order order, ZonedDateTime createdAt) {
        super(order, createdAt);
    }
}
