/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.resolver.base;

import io.geekshop.common.ApiType;
import io.geekshop.common.RequestContext;
import io.geekshop.common.utils.BeanMapper;
import io.geekshop.entity.AdministratorEntity;
import io.geekshop.exception.ForbiddenException;
import io.geekshop.service.AdministratorService;
import io.geekshop.service.UserService;
import io.geekshop.types.auth.CurrentUser;
import io.geekshop.types.user.User;
import lombok.extern.slf4j.Slf4j;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Slf4j
public abstract class BaseAuthQuery {
    protected final AdministratorService administratorService;
    protected final UserService userService;

    protected BaseAuthQuery(
            AdministratorService administratorService, UserService userService) {
        this.administratorService = administratorService;
        this.userService = userService;
    }

    /**
     * Returns information about the current authenticated user.
     */
    protected CurrentUser me(RequestContext ctx) {
        Long userId = ctx.getActiveUserId();
        if (ApiType.ADMIN.equals(ctx.getApiType())) {
            AdministratorEntity administratorEntity = this.administratorService.findOneEntityByUserId(userId);
            if (administratorEntity == null) throw new ForbiddenException();
        }
        if (userId == null) return null;
        User user = this.userService.findUserWithRolesById(userId);
        return BeanMapper.map(user, CurrentUser.class);
    }

}
