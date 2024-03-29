/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.resolver.shop;

import io.geekshop.common.RequestContext;
import io.geekshop.common.utils.TimeSpanUtil;
import io.geekshop.custom.security.Allow;
import io.geekshop.entity.OrderEntity;
import io.geekshop.exception.ForbiddenException;
import io.geekshop.service.CustomerService;
import io.geekshop.service.OrderService;
import io.geekshop.service.SessionService;
import io.geekshop.service.helpers.ServiceHelper;
import io.geekshop.service.helpers.order_state_machine.OrderState;
import io.geekshop.types.common.Permission;
import io.geekshop.types.order.Order;
import io.geekshop.types.shipping.ShippingMethodQuote;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Component
public class ShopOrderQuery extends BaseOrderAPI implements GraphQLQueryResolver {

    public ShopOrderQuery(OrderService orderService, SessionService sessionService, CustomerService customerService) {
        super(orderService, sessionService, customerService);
    }

    @Allow(Permission.Owner)
    public Order order(Long id, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        OrderEntity orderEntity = this.orderService.findOneWithItems(id);
        if (orderEntity == null) return null;
        if (ctx.isAuthorizedAsOwnerOnly()) {
            Long customerId = orderEntity.getCustomerId();
            if (customerId == null) return null;
            Long orderUserId = this.customerService.findOne(customerId).getUserId();
            if (Objects.equals(ctx.getActiveUserId(), orderUserId)) {
                return ServiceHelper.mapOrderEntityToOrder(orderEntity);
            }
        }
        return null;
    }

    @Allow(Permission.Owner)
    public Order activeOrder(DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        if (ctx.isAuthorizedAsOwnerOnly()) {
            Order sessionOrder = this.getOrderFromContext(ctx);
            if (sessionOrder != null) {
                OrderEntity orderEntity = this.orderService.findOneWithItems(sessionOrder.getId());
                if (orderEntity == null) return null;
                return ServiceHelper.mapOrderEntityToOrder(orderEntity);
            }
        }
        return null;
    }

    @Allow(Permission.Owner)
    public Order orderByCode(String code, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        if (ctx.isAuthorizedAsOwnerOnly()) {
            OrderEntity orderEntity = this.orderService.findOneWithItemsByCode(code);
            if (orderEntity != null) {
                // For guest Customers, allow access to the Order for the following
                // time period
                long anonymousAccessLimit = TimeSpanUtil.toMs("2h");
                long orderPlaced = orderEntity.getOrderPlacedAt() != null
                        ? orderEntity.getOrderPlacedAt().getTime() : 0;
                boolean activeUserMatches = orderEntity != null && orderEntity.getCustomerId() != null;
                if (activeUserMatches) {
                    Long orderUserId = this.customerService.findOne(orderEntity.getCustomerId()).getUserId();
                    if (!Objects.equals(orderUserId, ctx.getActiveUserId())) {
                        activeUserMatches = false;
                    }
                }
                long now = new Date().getTime();
                boolean isWithinAnonymousAccessLimit = now - orderPlaced < anonymousAccessLimit;
                if ((ctx.getActiveUserId() != null && activeUserMatches) ||
                        (ctx.getActiveUserId() == null && isWithinAnonymousAccessLimit)) {
                    return ServiceHelper.mapOrderEntityToOrder(orderEntity);
                }
            }
            // We throw even if the order does not exist, since giving a different response
            // opens the door to an enumeration attack to find valid order codes.
            throw new ForbiddenException();
        }
        return null;
    }

    @Allow(Permission.Owner)
    public List<ShippingMethodQuote> eligibleShippingMethods(DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        if (ctx.isAuthorizedAsOwnerOnly()) {
            Order sessionOrder = this.getOrderFromContext(ctx);
            if (sessionOrder != null) {
                return this.orderService.getEligibleShippingMethods(ctx, sessionOrder.getId());
            }
        }
        return Arrays.asList();
    }

    @Allow(Permission.Owner)
    public List<String> nextOrderStates(DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        if (ctx.isAuthorizedAsOwnerOnly()) {
            Order sessionOrder = this.getOrderFromContext(ctx, true);
            return this.orderService.getNextOrderStates(sessionOrder.getId())
                    .stream().map(OrderState::name).collect(Collectors.toList());
        }
        return Arrays.asList();
    }
}
