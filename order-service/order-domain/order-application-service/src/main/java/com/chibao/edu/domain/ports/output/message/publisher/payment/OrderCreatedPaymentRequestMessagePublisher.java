package com.chibao.edu.domain.ports.output.message.publisher.payment;

import com.chibao.edu.domain.event.OrderCreatedEvent;
import com.chibao.edu.domain.event.publisher.DomainEventPublisher;

public interface OrderCreatedPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCreatedEvent> {

}
