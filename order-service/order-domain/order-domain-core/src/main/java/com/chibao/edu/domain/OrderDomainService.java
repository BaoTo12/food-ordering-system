package com.chibao.edu.domain;

import com.chibao.edu.domain.entity.Order;
import com.chibao.edu.domain.entity.Restaurant;
import com.chibao.edu.domain.event.OrderCancelledEvent;
import com.chibao.edu.domain.event.OrderCreatedEvent;
import com.chibao.edu.domain.event.OrderPaidEvent;

import java.util.List;

public interface OrderDomainService {
    OrderCreatedEvent validateAndInitializeOrder(Order order, Restaurant restaurant);
    OrderPaidEvent payOrder(Order order);
    void approveOrder(Order order);

    OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages);

    void cancelOrder(Order order, List<String> failureMessages);
}
