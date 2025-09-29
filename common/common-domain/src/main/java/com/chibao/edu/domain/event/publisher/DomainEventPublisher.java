package com.chibao.edu.domain.event.publisher;

import com.chibao.edu.domain.event.DomainEvent;

public interface DomainEventPublisher<T extends DomainEvent> {
    void publish(T domainEvent);
}
