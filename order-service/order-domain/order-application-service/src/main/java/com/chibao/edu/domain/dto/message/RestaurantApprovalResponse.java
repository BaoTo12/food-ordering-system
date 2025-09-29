package com.chibao.edu.domain.dto.message;

import com.chibao.edu.domain.value_object.OrderApprovalStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantApprovalResponse {
    String id;
    String sagaId;
    String orderId;
    String restaurantId;
    Instant createdAt;
    OrderApprovalStatus orderApprovalStatus;
    List<String> failureMessages;
}
