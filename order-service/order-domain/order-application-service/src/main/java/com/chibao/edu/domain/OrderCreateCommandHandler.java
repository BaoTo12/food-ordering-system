package com.chibao.edu.domain;


import com.chibao.edu.domain.dto.create.CreateOrderCommand;
import com.chibao.edu.domain.dto.create.CreateOrderResponse;
import com.chibao.edu.domain.event.OrderCreatedEvent;
import com.chibao.edu.domain.mapper.OrderDataMapper;
import com.chibao.edu.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class OrderCreateCommandHandler {
    OrderCreateHelper orderCreateHelper;
    OrderDataMapper orderDataMapper;
    OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher;


    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        OrderCreatedEvent orderCreatedEvent = orderCreateHelper.persistOrder(createOrderCommand);
        log.info("Order is created with id: {}", orderCreatedEvent.getOrder().getId().getValue());
        orderCreatedPaymentRequestMessagePublisher.publish(orderCreatedEvent);
        return orderDataMapper.orderToCreateOrderResponse(orderCreatedEvent.getOrder(), "Order created successfully");
    }

}
