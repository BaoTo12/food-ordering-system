package com.chibao.edu.domain.value_object;

import java.util.UUID;

public class CustomerId extends BaseId<UUID>{
    public CustomerId(UUID value) {
        super(value);
    }
}
