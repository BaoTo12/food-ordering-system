package com.chibao.edu.domain.ports.input.service;

import com.chibao.edu.domain.dto.create.CreateOrderCommand;
import com.chibao.edu.domain.dto.create.CreateOrderResponse;
import com.chibao.edu.domain.dto.track.TrackOrderQuery;
import com.chibao.edu.domain.dto.track.TrackOrderResponse;
import jakarta.validation.Valid;

public interface OrderApplicationService {
    CreateOrderResponse createOrder(@Valid CreateOrderCommand createOrderCommand);
    TrackOrderResponse trackOrder(@Valid TrackOrderQuery trackOrderQuery);
}
