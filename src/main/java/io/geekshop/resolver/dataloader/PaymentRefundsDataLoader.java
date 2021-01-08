/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.resolver.dataloader;

import io.geekshop.common.utils.BeanMapper;
import io.geekshop.entity.RefundEntity;
import io.geekshop.mapper.RefundEntityMapper;
import io.geekshop.types.payment.Refund;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.dataloader.MappedBatchLoader;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * Created on Dec, 2020 by @author bobo
 */
@SuppressWarnings("Duplicates")
@RequiredArgsConstructor
public class PaymentRefundsDataLoader implements MappedBatchLoader<Long, List<Refund>> {
    private final RefundEntityMapper refundEntityMapper;

    @Override
    public CompletionStage<Map<Long, List<Refund>>> load(Set<Long> paymentIds) {
        return CompletableFuture.supplyAsync(() -> {
            QueryWrapper<RefundEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(RefundEntity::getPaymentId, paymentIds);
            List<RefundEntity> refundEntities = this.refundEntityMapper.selectList(queryWrapper);

            if (refundEntities.size() == 0) {
                Map<Long, List<Refund>> emptyMap = new HashMap<>();
                paymentIds.forEach(id -> emptyMap.put(id, new ArrayList<>()));
                return emptyMap;
            }

            Map<Long, List<Refund>> groupByPaymentId = refundEntities.stream()
                    .collect(Collectors.groupingBy(RefundEntity::getPaymentId,
                            Collectors.mapping(refundEntity -> BeanMapper.map(refundEntity, Refund.class),
                                    Collectors.toList()
                            )));

            return groupByPaymentId;
        });
    }
}
