/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.resolver.admin;

import io.geekshop.common.utils.BeanMapper;
import io.geekshop.custom.security.Allow;
import io.geekshop.entity.PromotionEntity;
import io.geekshop.service.PromotionService;
import io.geekshop.types.common.DeletionResponse;
import io.geekshop.types.common.Permission;
import io.geekshop.types.promotion.CreatePromotionInput;
import io.geekshop.types.promotion.Promotion;
import io.geekshop.types.promotion.UpdatePromotionInput;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class PromotionMutation implements GraphQLMutationResolver {

    private final PromotionService promotionService;

    @Allow(Permission.CreatePromotion)
    public Promotion createPromotion(CreatePromotionInput input, DataFetchingEnvironment dfe) {
        PromotionEntity promotionEntity = this.promotionService.createPromotion(input);
        return BeanMapper.map(promotionEntity, Promotion.class);
    }

    @Allow(Permission.UpdatePromotion)
    public Promotion updatePromotion(UpdatePromotionInput input, DataFetchingEnvironment dfe) {
        PromotionEntity promotionEntity = this.promotionService.updatePromotion(input);
        return BeanMapper.map(promotionEntity, Promotion.class);
    }

    @Allow(Permission.DeletePromotion)
    public DeletionResponse deletePromotion(Long id, DataFetchingEnvironment dfe) {
        return this.promotionService.softDeletePromotion(id);
    }
}
