package com.chibao.edu.domain.dto.create;


import com.chibao.edu.domain.value_object.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CreateOrderResponse {
    @NotNull
    UUID orderTrackingId;
    OrderStatus orderStatus;
    String message;
}
