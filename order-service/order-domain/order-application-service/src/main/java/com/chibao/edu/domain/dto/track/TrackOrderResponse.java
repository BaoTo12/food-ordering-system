package com.chibao.edu.domain.dto.track;

import com.chibao.edu.domain.value_object.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class TrackOrderResponse {
    private final UUID orderTrackingId;
    private final OrderStatus orderStatus;
    private final List<String> failureMessages;
}
