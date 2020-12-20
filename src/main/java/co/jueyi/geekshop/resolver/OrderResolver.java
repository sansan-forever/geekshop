/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver;

import co.jueyi.geekshop.common.ApiType;
import co.jueyi.geekshop.common.Constant;
import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.entity.FulfillmentEntity;
import co.jueyi.geekshop.entity.OrderEntity;
import co.jueyi.geekshop.service.HistoryService;
import co.jueyi.geekshop.service.OrderService;
import co.jueyi.geekshop.service.helpers.order_state_machine.OrderState;
import co.jueyi.geekshop.types.common.SortOrder;
import co.jueyi.geekshop.types.customer.Customer;
import co.jueyi.geekshop.types.history.HistoryEntryList;
import co.jueyi.geekshop.types.history.HistoryEntryListOptions;
import co.jueyi.geekshop.types.history.HistoryEntrySortParameter;
import co.jueyi.geekshop.types.order.Fulfillment;
import co.jueyi.geekshop.types.order.Order;
import co.jueyi.geekshop.types.order.OrderLine;
import co.jueyi.geekshop.types.payment.Payment;
import co.jueyi.geekshop.types.promotion.Promotion;
import co.jueyi.geekshop.types.shipping.ShippingMethod;
import graphql.kickstart.execution.context.GraphQLContext;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class OrderResolver implements GraphQLResolver<Order> {

    private final OrderService orderService;
    private final HistoryService historyService;

    public CompletableFuture<Customer> getCustomer(Order order, DataFetchingEnvironment dfe) {
        if (order.getCustomerId() == null) {
            CompletableFuture<Customer> completableFuture = new CompletableFuture<>();
            completableFuture.complete(null);
            return completableFuture;
        }

        final DataLoader<Long, Customer> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_ORDER_CUSTOMER);

        return dataLoader.load(order.getCustomerId());
    }

    public CompletableFuture<List<Promotion>> getPromotions(Order order, DataFetchingEnvironment dfe) {
        final DataLoader<Long, List<Promotion>> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_ORDER_PROMOTIONS);

        return dataLoader.load(order.getId());
    }

    public CompletableFuture<List<Payment>> getPayments(Order order, DataFetchingEnvironment dfe) {
        final DataLoader<Long, List<Payment>> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_ORDER_PAYMENTS);

        return dataLoader.load(order.getId());
    }

    public List<Fulfillment> getFulfillments(Order order, DataFetchingEnvironment dfe) {
        OrderEntity orderEntity = this.orderService.findOne(order.getId());
        List<FulfillmentEntity> fulfillmentEntities = this.orderService.getOrderFulfillments(orderEntity);
        return fulfillmentEntities.stream()
                .map(fulfillmentEntity -> BeanMapper.map(fulfillmentEntity, Fulfillment.class))
                .collect(Collectors.toList());
    }

    public CompletableFuture<ShippingMethod> getShippingMethod(Order order, DataFetchingEnvironment dfe) {
        if (order.getShippingMethodId() == null) {
            CompletableFuture<ShippingMethod> completableFuture = new CompletableFuture<>();
            completableFuture.complete(null);
            return completableFuture;
        }

        final DataLoader<Long, ShippingMethod> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_ORDER_SHIPPING_METHOD);

        return dataLoader.load(order.getShippingMethodId());
    }

    public HistoryEntryList getHistory(Order order, HistoryEntryListOptions options, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);

        if (options == null) {
            options = new HistoryEntryListOptions();
        }
        if (options.getSort() == null) {
            HistoryEntrySortParameter sort = new HistoryEntrySortParameter();
            sort.setCreatedAt(SortOrder.ASC);
            options.setSort(sort);
        }

        return this.historyService.getHistoryForOrder(
                order.getId(), ctx.getApiType() == ApiType.SHOP, options);
    }

    public List<String> getNextStates(Order order, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);

        if (ctx.getApiType() == ApiType.SHOP) return null; // Admin Only API

        return this.orderService.getNextOrderStates(order.getId()).stream()
                .map(OrderState::name).collect(Collectors.toList());
    }

    public CompletableFuture<List<OrderLine>> getLines(Order order, DataFetchingEnvironment dfe) {
        final DataLoader<Long, List<OrderLine>> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_ORDER_LINES);

        return dataLoader.load(order.getId());
    }
}
