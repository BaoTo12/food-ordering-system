package com.chibao.edu.domain;

import com.chibao.edu.domain.dto.create.CreateOrderCommand;
import com.chibao.edu.domain.dto.create.CreateOrderResponse;
import com.chibao.edu.domain.dto.track.TrackOrderQuery;
import com.chibao.edu.domain.dto.track.TrackOrderResponse;
import com.chibao.edu.domain.ports.input.service.OrderApplicationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


@Validated
@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
class OrderApplicationServiceImpl implements OrderApplicationService {
    OrderCreateCommandHandler orderCreateCommandHandler;
    OrderTrackCommandHandler orderTrackCommandHandler;


    @Override
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        return orderCreateCommandHandler.createOrder(createOrderCommand);
    }

    @Override
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        return orderTrackCommandHandler.trackOrder(trackOrderQuery);
    }
}
