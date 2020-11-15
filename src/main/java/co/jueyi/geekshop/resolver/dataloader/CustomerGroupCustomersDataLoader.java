/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.dataloader;

import co.jueyi.geekshop.service.CustomerGroupService;
import co.jueyi.geekshop.types.customer.CustomerList;
import co.jueyi.geekshop.types.customer.CustomerListOptions;
import org.dataloader.BatchLoaderEnvironment;
import org.dataloader.MappedBatchLoaderWithContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Created on Nov, 2020 by @author bobo
 */
public class CustomerGroupCustomersDataLoader implements MappedBatchLoaderWithContext<Long, CustomerList> {

    private final CustomerGroupService customerGroupService;

    public CustomerGroupCustomersDataLoader(CustomerGroupService customerGroupService) {
        this.customerGroupService = customerGroupService;
    }

    @Override
    public CompletionStage<Map<Long, CustomerList>> load(
            Set<Long> customerGroupIds, BatchLoaderEnvironment env) {
        return CompletableFuture.supplyAsync(() -> {
            Map<Long, CustomerList> resultMap = new HashMap<>();
            for (Long customerGroupId : customerGroupIds) {
                Map<Object, Object> optionsMap = env.getKeyContexts();
                CustomerListOptions options = (CustomerListOptions) optionsMap.get(customerGroupId);
                CustomerList customerList = this.customerGroupService.getGroupCustomers(customerGroupId, options);
                resultMap.put(customerGroupId, customerList);
            }
            return resultMap;
        });
    }
}
