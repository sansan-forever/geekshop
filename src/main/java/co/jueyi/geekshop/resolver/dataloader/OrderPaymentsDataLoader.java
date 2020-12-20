/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.dataloader;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.entity.PaymentEntity;
import co.jueyi.geekshop.mapper.PaymentEntityMapper;
import co.jueyi.geekshop.types.payment.Payment;
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
                            Collectors.mapping(paymentEntity -> BeanMapper.map(paymentEntity, Payment.class),
                                    Collectors.toList()
                            )));

            return groupByOrderId;
        });
    }
}
