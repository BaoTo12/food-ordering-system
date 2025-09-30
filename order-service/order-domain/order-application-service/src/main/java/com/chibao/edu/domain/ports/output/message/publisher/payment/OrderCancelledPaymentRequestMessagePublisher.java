package com.chibao.edu.domain.ports.output.message.publisher.payment;

import com.chibao.edu.domain.event.OrderCancelledEvent;
import com.chibao.edu.domain.event.publisher.DomainEventPublisher;

// ? An interface that defines how to publish a message when an order is created.
public interface OrderCancelledPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCancelledEvent> {

}
