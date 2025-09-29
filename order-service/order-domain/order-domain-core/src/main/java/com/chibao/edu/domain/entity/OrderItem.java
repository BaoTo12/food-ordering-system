package com.chibao.edu.domain.entity;

import com.chibao.edu.domain.value_object.Money;
import com.chibao.edu.domain.value_object.OrderId;
import com.chibao.edu.domain.value_object.OrderItemId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderItem extends BaseEntity<OrderItemId> {
    @NonFinal
    private OrderId orderId;
    Product product;
    int quantity;
    Money price;
    Money subTotal;

    // TODO implementation methods
    void initializeOrderItem(OrderId orderId, OrderItemId orderItemId) {
        super.setId(orderItemId);
        this.orderId = orderId;

    }

    boolean isPriceValid(){
        return price.isGreaterThanZero() &&
                price.equals(product.getPrice()) &&
                price.multiply(quantity).equals(subTotal);
    }
}
