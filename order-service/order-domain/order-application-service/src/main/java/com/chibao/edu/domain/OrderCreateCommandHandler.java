package com.chibao.edu.domain;


import com.chibao.edu.domain.dto.create.CreateOrderCommand;
import com.chibao.edu.domain.dto.create.CreateOrderResponse;
import com.chibao.edu.domain.entity.Customer;
import com.chibao.edu.domain.entity.Order;
import com.chibao.edu.domain.entity.Restaurant;
import com.chibao.edu.domain.event.OrderCreatedEvent;
import com.chibao.edu.domain.exception.OrderDomainException;
import com.chibao.edu.domain.mapper.OrderDataMapper;
import com.chibao.edu.domain.ports.output.repository.CustomerRepository;
import com.chibao.edu.domain.ports.output.repository.OrderRepository;
import com.chibao.edu.domain.ports.output.repository.RestaurantRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class OrderCreateCommandHandler {

    OrderDomainService orderDomainService;
    OrderRepository orderRepository;
    CustomerRepository customerRepository;
    RestaurantRepository restaurantRepository;
    OrderDataMapper orderDataMapper;
    ApplicationDomainPublisher applicationDomainPublisher;

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        checkCustomer(createOrderCommand.getCustomerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand);
        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitializeOrder(order, restaurant);
        Order orderResult =  saveOrder(order);
        log.info("Order is created with id: {}", orderResult.getId().getValue());
        applicationDomainPublisher.publish(orderCreatedEvent);
        return orderDataMapper.orderToCreateOrderResponse(orderResult);

    }

    private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {
        Restaurant restaurant = orderDataMapper.createOrderCommandToRestaurant(createOrderCommand);
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findRestaurantInformation(restaurant);
        if (optionalRestaurant.isEmpty()) {
            log.warn("Could not find restaurant with restaurant id: {}", restaurant.getId());
            throw new OrderDomainException("Could not find restaurant with restaurant id: " + restaurant.getId());
        }
        return optionalRestaurant.get();
    }

    private void checkCustomer(UUID customerId) {
        Optional<Customer> customer = customerRepository.findCustomer(customerId);
        if (customer.isEmpty()) {
            log.warn("Could not find customer with customerId: {}", customerId);
            throw new OrderDomainException("Could not find customer with customerId: " + customerId);
        }
    }

    // TODO Helpers
    private Order saveOrder(Order order){
        Order orderResult = orderRepository.save(order);
        if (orderResult == null){
            throw new OrderDomainException("Could not save order !");
        }
        log.info("Order is saved with id: {}", orderResult.getId());
        return orderResult;
    }
}
