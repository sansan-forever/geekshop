/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver;

import co.jueyi.geekshop.common.ApiType;
import co.jueyi.geekshop.common.Constant;
import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.types.address.Address;
import co.jueyi.geekshop.types.common.BooleanOperators;
import co.jueyi.geekshop.types.common.SortOrder;
import co.jueyi.geekshop.types.customer.Customer;
import co.jueyi.geekshop.types.customer.CustomerGroup;
import co.jueyi.geekshop.types.history.HistoryEntryFilterParameter;
import co.jueyi.geekshop.types.history.HistoryEntryList;
import co.jueyi.geekshop.types.history.HistoryEntryListOptions;
import co.jueyi.geekshop.types.history.HistoryEntrySortParameter;
import co.jueyi.geekshop.types.user.User;
import graphql.kickstart.execution.context.GraphQLContext;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.dataloader.DataLoader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
public class CustomerResolver implements GraphQLResolver<Customer> {
    public CompletableFuture<User> getUser(Customer customer, DataFetchingEnvironment dfe) {
        final DataLoader<Long, User> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_CUSTOMER_USER);

        return dataLoader.load(customer.getId());
    }


    public CompletableFuture<List<Address>> getAddresses(Customer customer, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        if (ApiType.SHOP.equals(ctx.getApiType()) && ctx.getActiveUserId() == null) {
            // Guest customers should not be able to see this data
            CompletableFuture<List<Address>> completableFuture = new CompletableFuture<>();
            completableFuture.complete(new ArrayList<>());
            return completableFuture;
        }

        final DataLoader<Long, List<Address>> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_CUSTOMER_ADDRESSES);

        return dataLoader.load(customer.getId());
    }

    public CompletableFuture<List<CustomerGroup>> getGroups(Customer customer, DataFetchingEnvironment dfe) {
        final DataLoader<Long, List<CustomerGroup>> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_CUSTOMER_GROUPS);

        return dataLoader.load(customer.getId());
    }

    public CompletableFuture<HistoryEntryList> getHistory(Customer customer, HistoryEntryListOptions options,
                                                          DataFetchingEnvironment dfe) {
        final DataLoader<Long, HistoryEntryList> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_CUSTOMER_HISTORY);

        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        if (options == null) {
            options = new HistoryEntryListOptions();
        }
        if (ApiType.SHOP.equals(ctx.getApiType())) { // show public only
            if (options.getFilter() == null) {
                options.setFilter(new HistoryEntryFilterParameter());
            }
            HistoryEntryFilterParameter filter = options.getFilter();
            BooleanOperators booleanOperators = new BooleanOperators();
            booleanOperators.setEq(true);
            filter.setIsPublic(booleanOperators);
        }

        if (options.getSort() == null) {
            HistoryEntrySortParameter sort = new HistoryEntrySortParameter();
            sort.setCreatedAt(SortOrder.ASC);
            options.setSort(sort);
        }

        return dataLoader.load(customer.getId(), options);
    }
}
