/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.service.helpers.payment_state_machine;

import io.geekshop.common.RequestContext;
import io.geekshop.common.fsm.FSM;
import io.geekshop.common.fsm.StateMachineConfig;
import io.geekshop.common.fsm.Transitions;
import io.geekshop.config.payment_method.PaymentMethodHandler;
import io.geekshop.entity.OrderEntity;
import io.geekshop.entity.PaymentEntity;
import io.geekshop.exception.IllegalOperationException;
import io.geekshop.service.ConfigService;
import io.geekshop.service.HistoryService;
import io.geekshop.service.args.CreateOrderHistoryEntryArgs;
import io.geekshop.types.history.HistoryEntryType;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class PaymentStateMachine {
    private final ConfigService configService;
    private final HistoryService historyService;

    private StateMachineConfig<PaymentState, PaymentTransitionData> config;

    public List<PaymentState> getNextStates(PaymentEntity paymentEntity) {
        FSM<PaymentState, PaymentTransitionData> fsm = new FSM<>(this.config, paymentEntity.getState());
        return fsm.getNextStates();
    }

    public void transition(
            RequestContext ctx, OrderEntity orderEntity, PaymentEntity paymentEntity, PaymentState state) {
        FSM<PaymentState, PaymentTransitionData> fsm = new FSM<>(this.config, paymentEntity.getState());
        PaymentTransitionData data = new PaymentTransitionData();
        data.setCtx(ctx);
        data.setOrderEntity(orderEntity);
        data.setPaymentEntity(paymentEntity);
        fsm.transitionTo(state, data);
        paymentEntity.setState(fsm.getCurrentState());
    }

    @PostConstruct
    void initConfig() {
        final List<PaymentMethodHandler> paymentMethodHandlers =
                this.configService.getPaymentOptions().getPaymentMethodHandlers();
        this.config = new StateMachineConfig<PaymentState, PaymentTransitionData>() {
            @Override
            public Transitions<PaymentState> getTransitions() {
                return getPaymentStateTransitions();
            }

            @Override
            public Object onTransitionStart(
                    PaymentState fromState, PaymentState toState, PaymentTransitionData data) {
                for(PaymentMethodHandler handler : paymentMethodHandlers) {
                    if (Objects.equals(data.getPaymentEntity().getMethod(), handler.getCode())) {
                        return handler.onStateTransitionStart(fromState, toState, data);
                    }
                }
                return "Payment handler with code { " + data.getPaymentEntity().getMethod() + " } not found";
            }

            @Override
            public void onTransitionEnd(
                    PaymentState fromState, PaymentState toState, PaymentTransitionData data) {
                CreateOrderHistoryEntryArgs args = new CreateOrderHistoryEntryArgs();
                args.setCtx(data.getCtx());
                args.setOrderId(data.getOrderEntity().getId());
                args.setType(HistoryEntryType.ORDER_PAYMENT_TRANSITION);
                args.setData(ImmutableMap.of(
                        "paymentId", data.getPaymentEntity().getId().toString(),
                        "from", fromState.name(),
                        "to", toState.name()));
                PaymentStateMachine.this.historyService.createHistoryEntryForOrder(args);
            }

            @Override
            public void onError(PaymentState fromState, PaymentState toState, String message) {
                String errorMessage = message;
                if (errorMessage == null) {
                    errorMessage = "Cannot transition Payment from { " + fromState + " } to { " + toState + " }";
                }
                throw new IllegalOperationException(errorMessage);
            }
        };
    }

    private Transitions<PaymentState> getPaymentStateTransitions() {
        Transitions<PaymentState> transitions = new Transitions<>();
        transitions.put(PaymentState.Created,
                Arrays.asList(
                        PaymentState.Authorized,
                        PaymentState.Settled,
                        PaymentState.Declined,
                        PaymentState.Error
                )
        );
        transitions.put(PaymentState.Authorized,
                Arrays.asList(
                        PaymentState.Settled,
                        PaymentState.Error
                )
        );
        transitions.put(PaymentState.Settled, Arrays.asList());
        transitions.put(PaymentState.Declined, Arrays.asList());
        transitions.put(PaymentState.Error, Arrays.asList());
        return transitions;
    }
}
