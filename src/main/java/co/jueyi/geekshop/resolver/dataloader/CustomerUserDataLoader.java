/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.dataloader;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.entity.CustomerEntity;
import co.jueyi.geekshop.entity.UserEntity;
import co.jueyi.geekshop.mapper.CustomerEntityMapper;
import co.jueyi.geekshop.mapper.UserEntityMapper;
import co.jueyi.geekshop.types.user.User;
import org.dataloader.MappedBatchLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * Created on Nov, 2020 by @author bobo
 */
public class CustomerUserDataLoader implements MappedBatchLoader<Long, User> {
    private final UserEntityMapper userEntityMapper;
    private final CustomerEntityMapper customerEntityMapper;

    public CustomerUserDataLoader(UserEntityMapper userEntityMapper, CustomerEntityMapper customerEntityMapper) {
        this.userEntityMapper = userEntityMapper;
        this.customerEntityMapper = customerEntityMapper;
    }

    @Override
    public CompletionStage<Map<Long, User>> load(Set<Long> customerIds) {
        return CompletableFuture.supplyAsync(() -> {
            Map<Long, User> customerUserMap = new HashMap<>();
            List<CustomerEntity> customerEntityList =
                    this.customerEntityMapper.selectBatchIds(customerIds);
            if (customerEntityList.size() == 0) return customerUserMap;
            List<Long> userIdList = customerEntityList.stream()
                    .map(CustomerEntity::getUserId)
                    .collect(Collectors.toList());

            List<UserEntity> userEntityList = this.userEntityMapper.selectBatchIds(userIdList);
            if (userEntityList.size() == 0) return customerUserMap;

            Map<Long, UserEntity> userEntityMap = userEntityList.stream()
                    .collect(Collectors.toMap(UserEntity::getId, userEntity -> userEntity));

            customerEntityList.forEach(customerEntity -> {
                Long customerId = customerEntity.getId();
                Long userId = customerEntity.getUserId();
                UserEntity userEntity = userEntityMap.get(userId);
                if (userEntity != null) {
                    User user = BeanMapper.patch(userEntity, User.class);
                    customerUserMap.put(customerId, user);
                }
            });

            return customerUserMap;
        });
    }
}
