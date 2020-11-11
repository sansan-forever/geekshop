package co.jueyi.geekshop.resolver.base;

import co.jueyi.geekshop.common.ApiType;
import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.entity.AdministratorEntity;
import co.jueyi.geekshop.exception.ForbiddenException;
import co.jueyi.geekshop.service.AdministratorService;
import co.jueyi.geekshop.service.UserService;
import co.jueyi.geekshop.types.auth.CurrentUser;
import co.jueyi.geekshop.types.user.User;
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
