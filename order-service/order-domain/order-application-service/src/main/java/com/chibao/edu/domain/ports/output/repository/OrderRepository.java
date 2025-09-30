package com.chibao.edu.domain.ports.output.repository;

import com.chibao.edu.domain.entity.Order;
import com.chibao.edu.domain.value_object.TrackingId;

import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findByTrackingId(TrackingId trackingId);
}
