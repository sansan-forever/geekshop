/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.shop;

import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.custom.security.Allow;
import co.jueyi.geekshop.entity.AddressEntity;
import co.jueyi.geekshop.entity.CustomerEntity;
import co.jueyi.geekshop.entity.OrderEntity;
import co.jueyi.geekshop.exception.IllegalOperationException;
import co.jueyi.geekshop.exception.UserInputException;
import co.jueyi.geekshop.service.CustomerService;
import co.jueyi.geekshop.service.OrderService;
import co.jueyi.geekshop.service.SessionService;
import co.jueyi.geekshop.service.helpers.ServiceHelper;
import co.jueyi.geekshop.service.helpers.order_state_machine.OrderState;
import co.jueyi.geekshop.types.common.CreateAddressInput;
import co.jueyi.geekshop.types.common.CreateCustomerInput;
import co.jueyi.geekshop.types.common.Permission;
import co.jueyi.geekshop.types.order.Order;
import co.jueyi.geekshop.types.order.OrderAddress;
import co.jueyi.geekshop.types.payment.PaymentInput;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Component
public class ShopOrderMutation extends BaseOrderAPI implements GraphQLMutationResolver {
    public ShopOrderMutation(
            OrderService orderService, SessionService sessionService, CustomerService customerService) {
        super(orderService, sessionService, customerService);
    }

    @Allow(Permission.Owner)
    public Order setOrderShippingAddress(CreateAddressInput input, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        if (ctx.isAuthorizedAsOwnerOnly()) {
            Order sessionOrder = this.getOrderFromContext(ctx);
            if (sessionOrder != null) {
                OrderEntity orderEntity = this.orderService.setShippingAddress(sessionOrder.getId(), input);
                return ServiceHelper.mapOrderEntityToOrder(orderEntity);
            }
        }
        return null;
    }

    @Allow(Permission.Owner)
    public Order setOrderBillingAddress(CreateAddressInput input, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        if (ctx.isAuthorizedAsOwnerOnly()) {
            Order sessionOrder = this.getOrderFromContext(ctx);
            if (sessionOrder != null) {
                OrderEntity orderEntity = this.orderService.setBillingAddress(sessionOrder.getId(), input);
                return ServiceHelper.mapOrderEntityToOrder(orderEntity);
            }
        }
        return null;
    }

    @Allow(Permission.Owner)
    public Order setOrderShippingMethod(Long shippingMethodId, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        if (ctx.isAuthorizedAsOwnerOnly()) {
            Order sessionOrder = this.getOrderFromContext(ctx);
            if (sessionOrder != null) {
                OrderEntity orderEntity = this.orderService.setShippingMethod(sessionOrder.getId(), shippingMethodId);
                return ServiceHelper.mapOrderEntityToOrder(orderEntity);
            }
        }
        return null;
    }

    @Allow(Permission.Owner)
    public Order transitionOrderToState(String state, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        if (ctx.isAuthorizedAsOwnerOnly()) {
            Order sessionOrder = this.getOrderFromContext(ctx, true);
            OrderState targetState = null;
            try {
                targetState = OrderState.valueOf(state);
            } catch (IllegalArgumentException ex) {
                throw new UserInputException(ex.getMessage());
            }
            OrderEntity orderEntity = this.orderService.transitionToState(ctx, sessionOrder.getId(), targetState);
            return ServiceHelper.mapOrderEntityToOrder(orderEntity);
        }
        return null;
    }

    @Allow(value = {Permission.UpdateOrder, Permission.Owner})
    public Order addItemToOrder(Long productVariantId, Integer quantity, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        Order sessionOrder = this.getOrderFromContext(ctx, true);
        OrderEntity orderEntity = this.orderService.addItemToOrder(sessionOrder.getId(), productVariantId, quantity);
        return ServiceHelper.mapOrderEntityToOrder(orderEntity);
    }

    @Allow(value = {Permission.UpdateOrder, Permission.Owner})
    public Order adjustOrderLine(Long orderLineId, Integer quantity, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        if (quantity == 0) {
            return this.removeOrderLine(orderLineId, dfe);
        }
        Order sessionOrder = this.getOrderFromContext(ctx, true);
        OrderEntity orderEntity =
                this.orderService.adjustOrderLine(sessionOrder.getId(), null, orderLineId, quantity);
        return ServiceHelper.mapOrderEntityToOrder(orderEntity);
    }

    @Allow(value = {Permission.UpdateOrder, Permission.Owner})
    public Order removeOrderLine(Long orderLineId, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        Order sessionOrder = this.getOrderFromContext(ctx, true);
        OrderEntity orderEntity = this.orderService.removeItemFromOrder(sessionOrder.getId(), orderLineId);
        return ServiceHelper.mapOrderEntityToOrder(orderEntity);
    }

    @Allow(value = {Permission.UpdateOrder, Permission.Owner})
    public Order removeAllOrderLines(DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        Order sessionOrder = this.getOrderFromContext(ctx, true);
        OrderEntity orderEntity = this.orderService.removeAllItemsFromOrder(sessionOrder.getId());
        return ServiceHelper.mapOrderEntityToOrder(orderEntity);
    }

    @Allow(value = {Permission.UpdateOrder, Permission.Owner})
    public Order applyCouponCode(String couponCode, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        Order sessionOrder = this.getOrderFromContext(ctx, true);
        OrderEntity orderEntity = this.orderService.applyCouponCode(ctx, sessionOrder.getId(), couponCode);
        return ServiceHelper.mapOrderEntityToOrder(orderEntity);
    }

    @Allow(value = {Permission.UpdateOrder, Permission.Owner})
    public Order removeCouponCode(String couponCode, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        Order sessionOrder = this.getOrderFromContext(ctx, true);
        OrderEntity orderEntity = this.orderService.removeCouponCode(ctx, sessionOrder.getId(), couponCode);
        return ServiceHelper.mapOrderEntityToOrder(orderEntity);
    }

    @Allow(value = {Permission.UpdateOrder, Permission.Owner})
    public Order addPaymentToOrder(PaymentInput input, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        if (ctx.isAuthorizedAsOwnerOnly()) {
            Order sessionOrder = this.getOrderFromContext(ctx);
            if (sessionOrder != null) {
                OrderEntity orderEntity = this.orderService.addPaymentToOrder(ctx, sessionOrder.getId(), input);
                if (!orderEntity.isActive()) {
                    if (orderEntity.getCustomerId() != null) {
                        List<AddressEntity> addressEntityList =
                                this.customerService.findAddressEntitiesByCustomerId(ctx, orderEntity.getCustomerId());
                        // If the Customer has no address yet, use the shipping address data
                        // to populate the initial default Address.
                        if (CollectionUtils.isEmpty(addressEntityList)) {
                            OrderAddress address =  orderEntity.getShippingAddress();
                            CreateAddressInput createAddressInput = BeanMapper.map(address, CreateAddressInput.class);
                            if (createAddressInput.getStreetLine1() == null) createAddressInput.setStreetLine1("");
                            if (createAddressInput.getStreetLine2() == null) createAddressInput.setStreetLine2("");
                            createAddressInput.setDefaultBillingAddress(true);
                            createAddressInput.setDefaultShippingAddress(true);

                            this.customerService.createAddress(ctx, orderEntity.getCustomerId(), createAddressInput);
                        }
                    }
                }
                if (!orderEntity.isActive() &&
                        Objects.equals(ctx.getSession().getActiveOrderId(), sessionOrder.getId())) {
                    this.sessionService.unsetActiveOrder(ctx.getSession());
                }
                return ServiceHelper.mapOrderEntityToOrder(orderEntity);
            }
        }
        return null;
    }

    @Allow(Permission.Owner)
    public Order setCustomerForOrder(CreateCustomerInput input, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        if (ctx.isAuthorizedAsOwnerOnly()) {
            if (ctx.getActiveUserId() != null) {
                throw new IllegalOperationException("Cannot set a Customer for the Order when already logged in");
            }
            Order sessionOrder = this.getOrderFromContext(ctx);
            if (sessionOrder != null) {
                CustomerEntity customerEntity = this.customerService.createOrUpdate(input, true);
                OrderEntity orderEntity =
                        this.orderService.addCustomerToOrder(sessionOrder.getId(), customerEntity.getId());
                return ServiceHelper.mapOrderEntityToOrder(orderEntity);
            }
        }
        return null;
    }
}
