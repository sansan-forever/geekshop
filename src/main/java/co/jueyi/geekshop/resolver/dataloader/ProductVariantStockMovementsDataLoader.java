/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.dataloader;

import co.jueyi.geekshop.service.StockMovementService;
import co.jueyi.geekshop.types.product.StockMovementListOptions;
import co.jueyi.geekshop.types.stock.StockMovementList;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoaderEnvironment;
import org.dataloader.MappedBatchLoaderWithContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Created on Nov, 2020 by @author bobo
 */
@RequiredArgsConstructor
public class ProductVariantStockMovementsDataLoader implements MappedBatchLoaderWithContext<Long, StockMovementList> {

    private final StockMovementService stockMovementService;

    @Override
    public CompletionStage<Map<Long, StockMovementList>> load(
            Set<Long> productVariantIds, BatchLoaderEnvironment environment) {
        return CompletableFuture.supplyAsync(() -> {
            Map<Long, StockMovementList> resultMap = new HashMap<>();
            for (Long variantId : productVariantIds) {
                Map<Object, Object> optionsMap = environment.getKeyContexts();
                StockMovementListOptions options = (StockMovementListOptions) optionsMap.get(variantId);
                StockMovementList stockMovementList=
                        stockMovementService.getStockMovementsByProductVaraintId(variantId, options);
                resultMap.put(variantId, stockMovementList);
            }
            return resultMap;
        });
    }
}
