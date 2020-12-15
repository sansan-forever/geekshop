/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.custom.security.Allow;
import co.jueyi.geekshop.entity.PromotionEntity;
import co.jueyi.geekshop.service.PromotionService;
import co.jueyi.geekshop.types.common.DeletionResponse;
import co.jueyi.geekshop.types.common.Permission;
import co.jueyi.geekshop.types.promotion.CreatePromotionInput;
import co.jueyi.geekshop.types.promotion.Promotion;
import co.jueyi.geekshop.types.promotion.UpdatePromotionInput;
import graphql.kickstart.tools.GraphQLMutationResolver;
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
    public Promotion createPromotion(CreatePromotionInput input) {
        PromotionEntity promotionEntity = this.promotionService.createPromotion(input);
        return BeanMapper.map(promotionEntity, Promotion.class);
    }

    @Allow(Permission.UpdatePromotion)
    public Promotion updatePromotion(UpdatePromotionInput input) {
        PromotionEntity promotionEntity = this.promotionService.updatePromotion(input);
        return BeanMapper.map(promotionEntity, Promotion.class);
    }

    @Allow(Permission.DeletePromotion)
    public DeletionResponse deletePromotion(Long id) {
        return this.promotionService.softDeletePromotion(id);
    }
}
