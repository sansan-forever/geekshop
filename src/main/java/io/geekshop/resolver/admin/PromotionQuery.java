/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.resolver.admin;

import io.geekshop.common.utils.BeanMapper;
import io.geekshop.custom.security.Allow;
import io.geekshop.entity.PromotionEntity;
import io.geekshop.service.PromotionService;
import io.geekshop.types.common.ConfigurableOperationDefinition;
import io.geekshop.types.common.Permission;
import io.geekshop.types.promotion.Promotion;
import io.geekshop.types.promotion.PromotionList;
import io.geekshop.types.promotion.PromotionListOptions;
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
