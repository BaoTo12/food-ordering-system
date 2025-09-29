package com.chibao.edu.domain.entity;

import com.chibao.edu.domain.value_object.Money;
import com.chibao.edu.domain.value_object.ProductId;
import lombok.Getter;

@Getter
public class Product extends BaseEntity<ProductId> {
    private String name;
    private Money price;

    public Product(ProductId productId, String name, Money price) {
        super.setId(productId);
        this.name = name;
        this.price = price;
    }

}
