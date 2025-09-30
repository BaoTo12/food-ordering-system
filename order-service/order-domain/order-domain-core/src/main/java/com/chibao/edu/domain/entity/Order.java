package com.chibao.edu.domain.entity;

import com.chibao.edu.domain.exception.OrderDomainException;
import com.chibao.edu.domain.value_object.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.util.List;
import java.util.UUID;

// this is aggregate root for order service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
@SuperBuilder
public class Order extends AggregateRoot<OrderId> {
    CustomerId customerId;
    RestaurantId restaurantId;
    StreetAddress deliverAddress;
    Money price; // ? total price
    List<OrderItem> items;

    @NonFinal
    TrackingId trackingId;
    @NonFinal
    OrderStatus orderStatus;
    @NonFinal
    List<String> failureMessages;

    // TODO implementation methods
    // * ***********************************************
    public void initializeOrder() {
        setId(new OrderId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        orderStatus = OrderStatus.PENDING;
        initializeOrderItems();
    }
    // TODO State changing methods
    // * ***********************************************
    public void pay(){
        if (orderStatus != OrderStatus.PENDING){
            throw new OrderDomainException("the order is not in correct state for payment!");
        }
        orderStatus = OrderStatus.PAID;
    }
    // * ***********************************************
    public void approve(){
        if (orderStatus != OrderStatus.PAID){
            throw new OrderDomainException("the order is not in correct state for approval!");
        }
        orderStatus = OrderStatus.APPROVED;
    }
    // * ***********************************************
    public void initCancel(List<String> failureMessages){
        if (orderStatus != OrderStatus.PAID){
            throw new OrderDomainException("the order is not in correct state for cancel initialization!");
        }
        orderStatus = OrderStatus.CANCELLING;
        updateFailureMessages(failureMessages);
    }
    // * ***********************************************
    public void cancel(){
        if (!(orderStatus == OrderStatus.CANCELLING || orderStatus == OrderStatus.PENDING)){
            throw new OrderDomainException("the order is not in correct state for cancel operation!");
        }
        orderStatus = OrderStatus.CANCELLED;
    }

    // TODO Helpers
    private void updateFailureMessages(List<String> failureMessages){
        if (this.failureMessages != null && failureMessages != null){
            this.failureMessages.addAll(failureMessages.stream().filter(String::isEmpty).toList());
        }
    }
    public void validateOrder() {
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }

    private void validateItemsPrice() {
        Money orderItemsTotal = items.stream().map(orderItem -> {
            // ? validate each item price
            validateItemPrice(orderItem);
            return orderItem.getSubTotal();
        }).reduce(Money.ZERO, Money::add);

        // ? validate total price of all items
        if (!price.equals(orderItemsTotal)) {
            throw new OrderDomainException("Total price: " + price.getAmount()
                    + "is not equal to Order items total: " + orderItemsTotal.getAmount() + "!"
            );
        }
    }

    private void validateItemPrice(OrderItem orderItem) {
        if (!orderItem.isPriceValid()) {
            throw new OrderDomainException("Order item price: " + orderItem.getPrice()
                    + "is not valid for product" + orderItem.getProduct().getId().getValue());
        }
    }

    private void validateTotalPrice() {
        if (price == null || price.isGreaterThanZero()) {
            throw new OrderDomainException("Total price must be greater than zero!");
        }
    }

    private void validateInitialOrder() {
        if (orderStatus == null || this.getId() == null) {
            throw new OrderDomainException("Order is not in correct state for initialization!");
        }
    }

    private void initializeOrderItems() {
        long itemId = 1;
        for (OrderItem orderItem : items) {
            orderItem.initializeOrderItem(this.getId(), new OrderItemId(itemId++));
        }
    }
}
