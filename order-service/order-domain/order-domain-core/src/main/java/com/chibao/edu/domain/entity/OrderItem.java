package com.chibao.edu.domain.entity;

import com.chibao.edu.domain.value_object.Money;
import com.chibao.edu.domain.value_object.OrderId;
import com.chibao.edu.domain.value_object.OrderItemId;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderItem extends BaseEntity<OrderItemId>{
    private OrderId orderId;
    private final Product product;
    private final int quantity;
    private final Money money;
    private final Money subTotal;

}
