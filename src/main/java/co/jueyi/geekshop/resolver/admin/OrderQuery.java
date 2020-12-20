/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.custom.security.Allow;
import co.jueyi.geekshop.entity.OrderEntity;
import co.jueyi.geekshop.service.OrderService;
import co.jueyi.geekshop.service.helpers.ServiceHelper;
import co.jueyi.geekshop.types.common.Permission;
import co.jueyi.geekshop.types.order.Order;
import co.jueyi.geekshop.types.order.OrderList;
import co.jueyi.geekshop.types.order.OrderListOptions;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class OrderQuery implements GraphQLQueryResolver {

    private final OrderService orderService;

    /**
     * Query
     */
    @Allow(Permission.ReadOrder)
    public Order order(Long id, DataFetchingEnvironment dfe) {
        OrderEntity orderEntity = this.orderService.findOne(id);
        if (orderEntity == null) return null;
        return ServiceHelper.mapOrderEntityToOrder(orderEntity);
    }

    @Allow(Permission.ReadOrder)
    public OrderList orders(OrderListOptions options, DataFetchingEnvironment dfe) {
        return this.orderService.findAll(options);
    }
}
