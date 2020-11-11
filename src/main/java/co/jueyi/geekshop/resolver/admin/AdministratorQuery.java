package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.custom.security.Allow;
import co.jueyi.geekshop.entity.AdministratorEntity;
import co.jueyi.geekshop.service.AdministratorService;
import co.jueyi.geekshop.types.administrator.Administrator;
import co.jueyi.geekshop.types.administrator.AdministratorList;
import co.jueyi.geekshop.types.administrator.AdministratorListOptions;
import co.jueyi.geekshop.types.common.Permission;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class AdministratorQuery implements GraphQLQueryResolver {

    private final AdministratorService administratorService;

    @Allow(Permission.ReadAdministrator)
    public AdministratorList administrators(AdministratorListOptions options, DataFetchingEnvironment dfe) {
        return administratorService.findAll(options);
    }

    @Allow(Permission.ReadAdministrator)
    public Administrator administrator(Long id, DataFetchingEnvironment dfe) {
        AdministratorEntity administratorEntity = administratorService.findOneEntity(id);
        if (administratorEntity == null) return null;
        return BeanMapper.map(administratorEntity, Administrator.class);
    }
}
