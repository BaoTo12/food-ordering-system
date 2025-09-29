package com.chibao.edu.domain.dto.create;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderItem {
    @NotNull
    UUID productId;
    @NotNull
    Integer quantity;
    @NotNull
    BigDecimal price;
    @NotNull
    BigDecimal subTotal;
}
