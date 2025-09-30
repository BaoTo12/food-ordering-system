package com.chibao.edu.domain.value_object;

import java.util.UUID;

public class ProductId extends BaseId<UUID>{
    public ProductId(UUID value) {
        super(value);
    }
}
