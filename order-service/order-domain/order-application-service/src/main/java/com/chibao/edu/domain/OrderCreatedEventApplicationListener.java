package com.chibao.edu.domain;

import com.chibao.edu.domain.event.OrderCreatedEvent;
import com.chibao.edu.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class OrderCreatedEventApplicationListener {
    private final OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher;
    public OrderCreatedEventApplicationListener(OrderCreatedPaymentRequestMessagePublisher
                                                        orderCreatedPaymentRequestMessagePublisher){
        this.orderCreatedPaymentRequestMessagePublisher = orderCreatedPaymentRequestMessagePublisher;
    }

    // ? @EventListener listens to normal events
    // ? @TransactionalEventListener listens to events that are in transactions
    // * that means it will be called if the transactions are in successful, rollback,.. state.
    // *  BEFORE_COMMIT,   // gọi trước khi transaction commit
    // *  AFTER_COMMIT,    // gọi sau khi transaction commit thành công (mặc định)
    // *  AFTER_ROLLBACK,  // gọi nếu transaction rollback
    // *  AFTER_COMPLETION // gọi sau khi transaction kết thúc (commit hoặc rollback)
    @TransactionalEventListener
    void process(OrderCreatedEvent orderCreatedEvent){
        orderCreatedPaymentRequestMessagePublisher.publish(orderCreatedEvent);
    }

}
