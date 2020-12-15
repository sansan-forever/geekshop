/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.custom.security.Allow;
import co.jueyi.geekshop.entity.PromotionEntity;
import co.jueyi.geekshop.service.PromotionService;
import co.jueyi.geekshop.types.common.ConfigurableOperationDefinition;
import co.jueyi.geekshop.types.common.Permission;
import co.jueyi.geekshop.types.promotion.Promotion;
import co.jueyi.geekshop.types.promotion.PromotionList;
import co.jueyi.geekshop.types.promotion.PromotionListOptions;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class PromotionQuery implements GraphQLQueryResolver {

    private final PromotionService promotionService;

    @Allow(Permission.ReadPromotion)
    public Promotion promotion(Long id, DataFetchingEnvironment dfe) {
        PromotionEntity promotionEntity = this.promotionService.findOne(id);
        if (promotionEntity == null) return null;
        return BeanMapper.map(promotionEntity, Promotion.class);
    }

    @Allow(Permission.ReadPromotion)
    public PromotionList promotions(PromotionListOptions options, DataFetchingEnvironment dfe) {
        return this.promotionService.findAll(options);
    }
    @Allow(Permission.ReadPromotion)
    public List<ConfigurableOperationDefinition> promotionConditions(DataFetchingEnvironment dfe) {
        return this.promotionService.getPromotionConditions();
    }

    @Allow(Permission.ReadPromotion)
    public List<ConfigurableOperationDefinition> promotionActions(DataFetchingEnvironment dfe) {
        return this.promotionService.getPromotionActions();
    }
}
