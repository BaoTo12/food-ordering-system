package com.chibao.edu.domain;

import com.chibao.edu.domain.dto.track.TrackOrderQuery;
import com.chibao.edu.domain.dto.track.TrackOrderResponse;
import com.chibao.edu.domain.entity.Order;
import com.chibao.edu.domain.exception.OrderNotFoundException;
import com.chibao.edu.domain.mapper.OrderDataMapper;
import com.chibao.edu.domain.ports.output.repository.OrderRepository;
import com.chibao.edu.domain.value_object.TrackingId;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class OrderTrackCommandHandler {
    OrderDataMapper orderDataMapper;
    OrderRepository orderRepository;


    @Transactional(readOnly = true)
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        Optional<Order> orderResult = orderRepository.findByTrackingId(new TrackingId(trackOrderQuery.getOrderTrackingId()));
        if (orderResult.isEmpty()) {
            log.warn("Could not find order with tracking id: {}", trackOrderQuery.getOrderTrackingId());
            throw new OrderNotFoundException("Could not find order with tracking id: " + trackOrderQuery.getOrderTrackingId());

        }
        return orderDataMapper.orderToTrackOrderResponse(orderResult.get());
    }
}
