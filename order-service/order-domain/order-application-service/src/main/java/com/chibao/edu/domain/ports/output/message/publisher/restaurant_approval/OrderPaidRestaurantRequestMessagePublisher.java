package com.chibao.edu.domain.ports.output.message.publisher.restaurant_approval;

import com.chibao.edu.domain.event.OrderPaidEvent;
import com.chibao.edu.domain.event.publisher.DomainEventPublisher;

public interface OrderPaidRestaurantRequestMessagePublisher extends DomainEventPublisher<OrderPaidEvent> {
}
