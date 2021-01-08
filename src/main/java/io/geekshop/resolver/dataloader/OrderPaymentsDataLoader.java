/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.resolver.dataloader;

import io.geekshop.common.utils.BeanMapper;
import io.geekshop.entity.PaymentEntity;
import io.geekshop.mapper.PaymentEntityMapper;
import io.geekshop.types.payment.Payment;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.dataloader.MappedBatchLoader;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * Created on Dec, 2020 by @author bobo
 */
@SuppressWarnings("Duplicates")
public class OrderPaymentsDataLoader implements MappedBatchLoader<Long, List<Payment>> {

    private final PaymentEntityMapper paymentEntityMapper;

    public OrderPaymentsDataLoader(PaymentEntityMapper paymentEntityMapper) {
        this.paymentEntityMapper = paymentEntityMapper;
    }

    @Override
    public CompletionStage<Map<Long, List<Payment>>> load(Set<Long> orderIds) {
        return CompletableFuture.supplyAsync(() -> {
            QueryWrapper<PaymentEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(PaymentEntity::getOrderId, orderIds);
            List<PaymentEntity> paymentEntities = this.paymentEntityMapper.selectList(queryWrapper);

            if (paymentEntities.size() == 0) {
                Map<Long, List<Payment>> emptyMap = new HashMap<>();
                orderIds.forEach(id -> emptyMap.put(id, new ArrayList<>()));
                return emptyMap;
            }

            Map<Long, List<Payment>> groupByOrderId = paymentEntities.stream()
                    .collect(Collectors.groupingBy(PaymentEntity::getOrderId,
                            Collectors.mapping(paymentEntity -> {
                                Payment payment = BeanMapper.map(paymentEntity, Payment.class);
                                if (paymentEntity.getState() != null) {
                                    payment.setState(paymentEntity.getState().name());
                                }
                                return payment;
                                },
                                    Collectors.toList()
                            )));

            return groupByOrderId;
        });
    }
}
