package com.chibao.edu.domain;

import com.chibao.edu.domain.event.OrderCreatedEvent;
import com.chibao.edu.domain.event.publisher.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApplicationDomainPublisher
        implements ApplicationEventPublisherAware, DomainEventPublisher<OrderCreatedEvent> {
    private ApplicationEventPublisher applicationEventPublisher;

    // ? ApplicationEventPublisherAware helps a Spring bean to get a ApplicationEventPublisher from Ioc Container
    // ? ApplicationEventPublisher is an interface that helps you publish an event to all listeners
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(OrderCreatedEvent domainEvent) {
        this.applicationEventPublisher.publishEvent(domainEvent);
        log.info("OrderCreatedEvent is published for order id: {}", domainEvent.getOrder().getId().getValue());
    }
}
