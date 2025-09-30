package com.chibao.edu.domain.ports.output.message.publisher.payment;

import com.chibao.edu.domain.event.OrderCancelledEvent;
import com.chibao.edu.domain.event.publisher.DomainEventPublisher;

public interface OrderCancelledPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCancelledEvent> {

}
