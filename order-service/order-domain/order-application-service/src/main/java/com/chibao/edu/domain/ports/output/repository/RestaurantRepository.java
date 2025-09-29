package com.chibao.edu.domain.ports.output.repository;

import com.chibao.edu.domain.entity.Restaurant;

import java.util.Optional;

public interface RestaurantRepository {
    Optional<Restaurant> findRestaurantInformation(Restaurant restaurant);
}
