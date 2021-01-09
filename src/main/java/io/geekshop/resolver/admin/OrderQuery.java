/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.resolver.admin;

import io.geekshop.custom.security.Allow;
import io.geekshop.entity.OrderEntity;
import io.geekshop.service.OrderService;
import io.geekshop.service.helpers.ServiceHelper;
import io.geekshop.types.common.Permission;
import io.geekshop.types.order.Order;
import io.geekshop.types.order.OrderList;
import io.geekshop.types.order.OrderListOptions;
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
    public Order orderByAdmin(Long id, DataFetchingEnvironment dfe) {
        OrderEntity orderEntity = this.orderService.findOneWithItems(id);
        if (orderEntity == null) return null;
        return ServiceHelper.mapOrderEntityToOrder(orderEntity);
    }

    @Allow(Permission.ReadOrder)
    public OrderList orders(OrderListOptions options, DataFetchingEnvironment dfe) {
        return this.orderService.findAllWithItems(options);
    }
}
