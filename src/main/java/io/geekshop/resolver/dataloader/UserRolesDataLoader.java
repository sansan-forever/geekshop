/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.resolver.dataloader;

import io.geekshop.common.utils.BeanMapper;
import io.geekshop.entity.RoleEntity;
import io.geekshop.entity.UserRoleJoinEntity;
import io.geekshop.mapper.RoleEntityMapper;
import io.geekshop.mapper.UserRoleJoinEntityMapper;
import io.geekshop.types.role.Role;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.dataloader.MappedBatchLoader;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * Created on Nov, 2020 by @author bobo
 */
public class UserRolesDataLoader implements MappedBatchLoader<Long, List<Role>> {
    private final UserRoleJoinEntityMapper userRoleJoinEntityMapper;
    private final RoleEntityMapper roleEntityMapper;


    public UserRolesDataLoader(UserRoleJoinEntityMapper userRoleJoinEntityMapper, RoleEntityMapper roleEntityMapper) {
        this.userRoleJoinEntityMapper = userRoleJoinEntityMapper;
        this.roleEntityMapper = roleEntityMapper;
    }

    @Override
    public CompletionStage<Map<Long, List<Role>>> load(Set<Long> userIds) {
        return CompletableFuture.supplyAsync(() -> {
            Map<Long, List<Role>> userRoleMap = new HashMap<>();
            userIds.forEach(id -> userRoleMap.put(id, new ArrayList<>()));

            QueryWrapper<UserRoleJoinEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(UserRoleJoinEntity::getUserId, userIds);
            List<UserRoleJoinEntity> userRoleJoinEntityList = userRoleJoinEntityMapper.selectList(queryWrapper);
            if (CollectionUtils.isEmpty(userRoleJoinEntityList)) return userRoleMap;

            Set<Long> roleIds =
                    userRoleJoinEntityList.stream().map(UserRoleJoinEntity::getRoleId).collect(Collectors.toSet());
            List<RoleEntity> roleEntityList = roleEntityMapper.selectBatchIds(roleIds);
            if (CollectionUtils.isEmpty(roleEntityList)) return userRoleMap;

            Map<Long, RoleEntity> roleEntityMap = roleEntityList.stream()
                    .collect(Collectors.toMap(RoleEntity::getId, roleEntity -> roleEntity));

            userRoleJoinEntityList.forEach(userRoleJoinEntity -> {
                Long userId = userRoleJoinEntity.getUserId();
                Long roleId = userRoleJoinEntity.getRoleId();
                List<Role> roleList = userRoleMap.get(userId);
                RoleEntity roleEntity = roleEntityMap.get(roleId);
                Role role = BeanMapper.patch(roleEntity, Role.class);
                roleList.add(role);
            });

            return userRoleMap;
        });
    }
}
