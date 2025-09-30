package com.chibao.edu.domain.entity;

import com.chibao.edu.domain.value_object.CustomerId;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class Customer extends AggregateRoot<CustomerId>{
    private String username;
    private String firstName;
    private String lastName;

    public Customer(){

    }
}
