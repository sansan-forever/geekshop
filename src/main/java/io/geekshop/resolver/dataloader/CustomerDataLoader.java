/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.resolver.dataloader;

import io.geekshop.common.utils.BeanMapper;
import io.geekshop.entity.CustomerEntity;
import io.geekshop.mapper.CustomerEntityMapper;
import io.geekshop.types.customer.Customer;
import org.dataloader.MappedBatchLoader;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * Created on Dec, 2020 by @author bobo
 */
public class CustomerDataLoader implements MappedBatchLoader<Long, Customer> {
    private final CustomerEntityMapper customerEntityMapper;

    public CustomerDataLoader(CustomerEntityMapper customerEntityMapper) {
        this.customerEntityMapper = customerEntityMapper;
    }

    @Override
    public CompletionStage<Map<Long, Customer>> load(Set<Long> customerIds) {
        return CompletableFuture.supplyAsync(() -> {
            List<CustomerEntity> customerEntities =
                    this.customerEntityMapper.selectBatchIds(customerIds);
            List<Customer> customers = customerEntities.stream()
                    .map(customerEntity -> BeanMapper.map(customerEntity, Customer.class))
                    .collect(Collectors.toList());
            Map<Long, Customer> customerMap = customers.stream()
                    .collect(Collectors.toMap(Customer::getId, c -> c));
            return customerMap;
        });
    }
}
