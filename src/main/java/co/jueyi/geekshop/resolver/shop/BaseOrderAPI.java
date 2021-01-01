/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.shop;

import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.entity.OrderEntity;
import co.jueyi.geekshop.exception.InternalServerError;
import co.jueyi.geekshop.service.CustomerService;
import co.jueyi.geekshop.service.OrderService;
import co.jueyi.geekshop.service.SessionService;
import co.jueyi.geekshop.types.order.Order;
import lombok.RequiredArgsConstructor;

/**
 * Created on Dec, 2020 by @author bobo
 */

@RequiredArgsConstructor
public abstract class BaseOrderAPI {
    protected final OrderService orderService;
    protected final SessionService sessionService;
    protected final CustomerService customerService;

    protected Order getOrderFromContext(RequestContext ctx) {
        return this.getOrderFromContext(ctx, false);
    }

    protected Order getOrderFromContext(RequestContext ctx, boolean createIfNotExists) {
        if (ctx.getSession() == null) {
            throw new InternalServerError("No active session");
        }
        OrderEntity orderEntity = ctx.getSession().getActiveOrderId() != null
                ? this.orderService.findOne(ctx.getSession().getActiveOrderId())
                : null;
        if (orderEntity != null && !orderEntity.isActive()) {
            // edge case where an inactive order may not have been
            // removed from the session, i.e. the regular process was interrupted
            this.sessionService.unsetActiveOrder(ctx.getSession());
            orderEntity = null;
        }
        if (orderEntity == null) {
            if (ctx.getActiveUserId() != null) {
                orderEntity = this.orderService.getActiveOrderForUser(ctx.getActiveUserId(), false);
            }

            if (orderEntity == null && createIfNotExists) {
                orderEntity = this.orderService.create(ctx, ctx.getActiveUserId());
            }

            if (orderEntity != null) {
                this.sessionService.setActiveOrder(ctx.getSession(), orderEntity.getId());
            }
        }
        if (orderEntity == null) return null;
        return BeanMapper.map(orderEntity, Order.class);
    }
}
