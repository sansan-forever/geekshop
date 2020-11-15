/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver;

import co.jueyi.geekshop.common.Constant;
import co.jueyi.geekshop.types.customer.CustomerGroup;
import co.jueyi.geekshop.types.customer.CustomerList;
import co.jueyi.geekshop.types.customer.CustomerListOptions;
import graphql.kickstart.execution.context.GraphQLContext;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.dataloader.DataLoader;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
public class CustomerGroupResolver implements GraphQLResolver<CustomerGroup> {
    public CompletableFuture<CustomerList> getCustomers(
            CustomerGroup customerGroup, CustomerListOptions options, DataFetchingEnvironment dfe) {
        final DataLoader<Long, CustomerList> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_CUSTOMER_GROUP_CUSTOMERS);

        return dataLoader.load(customerGroup.getId(), options);
    }
}
