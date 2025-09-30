package com.chibao.edu.domain.dto.create;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CreateOrderCommand {
    @NonNull
    UUID customerId;
    @NonNull
    UUID restaurantId;
    @NonNull
    BigDecimal price;
    @NonNull
    List<OrderItem> items;
    @NonNull
    OrderAddress address;
}
