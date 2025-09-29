package com.chibao.edu.domain.dto.create;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderAddress {
    @NotNull
    String street;
    @NotNull
    @Max(value = 10)
    String postalCode;
    @NotNull
    @Max(value = 50)
    String city;
}
