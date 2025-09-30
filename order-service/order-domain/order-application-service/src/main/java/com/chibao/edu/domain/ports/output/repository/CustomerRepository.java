package com.chibao.edu.domain.ports.output.repository;

import com.chibao.edu.domain.entity.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {
    Optional<Customer> findCustomer(UUID customerId);
}
