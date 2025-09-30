package com.chibao.edu.domain.dto.message;


import com.chibao.edu.domain.value_object.PaymentStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentResponse {
    String id;
    String sagaId;
    String orderId;
    String paymentId;
    String customerId;
    BigDecimal price;
    Instant createdAt;
    PaymentStatus paymentStatus;
    List<String> failureMessages;
}
