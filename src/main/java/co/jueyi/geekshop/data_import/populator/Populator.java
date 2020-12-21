/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.data_import.populator;

import co.jueyi.geekshop.common.ApiType;
import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.common.utils.NormalizeUtil;
import co.jueyi.geekshop.config.shipping_method.ShippingCalculator;
import co.jueyi.geekshop.config.shipping_method.ShippingEligibilityChecker;
import co.jueyi.geekshop.data_import.CollectionDefinition;
import co.jueyi.geekshop.data_import.FacetValueCollectionFilterDefinition;
import co.jueyi.geekshop.data_import.InitialData;
import co.jueyi.geekshop.data_import.ShippingMethodData;
import co.jueyi.geekshop.data_import.asset_importer.AssetImporter;
import co.jueyi.geekshop.entity.AssetEntity;
import co.jueyi.geekshop.entity.CollectionEntity;
import co.jueyi.geekshop.entity.FacetValueEntity;
import co.jueyi.geekshop.service.CollectionService;
import co.jueyi.geekshop.service.FacetValueService;
import co.jueyi.geekshop.service.ShippingMethodService;
import co.jueyi.geekshop.types.collection.CreateCollectionInput;
import co.jueyi.geekshop.types.common.ConfigArgInput;
import co.jueyi.geekshop.types.common.ConfigurableOperationInput;
import co.jueyi.geekshop.types.shipping.CreateShippingMethodInput;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Responsible for populating the database with initial data.
 *
 * Created on Nov, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class Populator {
    private final CollectionService collectionService;
    private final FacetValueService facetValueService;
    private final AssetImporter assetImporter;
    private final ObjectMapper objectMapper;
    private final ShippingMethodService shippingMethodService;
    private final ShippingEligibilityChecker defaultShippingEligibilityChecker;
    private final ShippingCalculator defaultShippingCalculator;
    // TODO searchService

    /**
     * Should be run *before* populating the products.
     */
    public void populateInitialData(InitialData data) {
        this.populateShippingMethods(data.getShippingMethods());
    }

    private void populateShippingMethods(List<ShippingMethodData> shippingMethods) {
        for(ShippingMethodData method: shippingMethods) {
            CreateShippingMethodInput input = new CreateShippingMethodInput();
            ConfigurableOperationInput checker = new ConfigurableOperationInput();
            checker.setCode(defaultShippingEligibilityChecker.getCode());
            ConfigArgInput configArgInput = new ConfigArgInput();
            configArgInput.setName("orderMinimum");
            configArgInput.setValue("0");
            checker.getArguments().add(configArgInput);
            input.setChecker(checker);

            ConfigurableOperationInput calculator = new ConfigurableOperationInput();
            calculator.setCode(defaultShippingCalculator.getCode());
            configArgInput = new ConfigArgInput();
            configArgInput.setName("rate");
            configArgInput.setValue(method.getPrice().toString());
            calculator.getArguments().add(configArgInput);
            input.setCalculator(calculator);

            input.setDescription(method.getName());
            input.setCode(NormalizeUtil.normalizeString(method.getName(), "-"));

            this.shippingMethodService.create(input);
        }
    }

    /**
     * Should be run *after* the products have been populated, otherwise the expected FacetValues will not yet exist.
     */
    public void populateCollections(InitialData data) {
        RequestContext ctx = this.createRequestContext(data);

        List<FacetValueEntity> allFacetValues = this.facetValueService.findAll();
        Map<String, CollectionEntity> collectionMap = new HashMap<>();
        for(CollectionDefinition collectionDef : data.getColletions()) {
            CollectionEntity parent = null;
            if (collectionDef.getParentName() != null) {
                parent = collectionMap.get(collectionDef.getParentName());
            }
            Long parentId = null;
            if (parent != null) {
                parentId = parent.getId();
            }
            List<AssetEntity> assets = this.assetImporter.getAssets(collectionDef.getAssetPaths()).getAssets();

            CreateCollectionInput createCollectionInput = new CreateCollectionInput();
            createCollectionInput.setName(collectionDef.getName());
            createCollectionInput.setDescription(collectionDef.getDescription());
            if (createCollectionInput.getDescription() == null) {
                createCollectionInput.setDescription("");
            }
            createCollectionInput.setSlug(collectionDef.getSlug());
            if (createCollectionInput.getSlug() == null) {
                createCollectionInput.setSlug(collectionDef.getName());
            }
            createCollectionInput.setPrivateOnly(collectionDef.isPrivateOnly());
            createCollectionInput.setParentId(parentId);
            createCollectionInput.setAssetIds(assets.stream().map(a -> a.getId()).collect(Collectors.toList()));
            if (assets.size() > 0) {
                createCollectionInput.setFeaturedAssetId(assets.get(0).getId());
            }
            createCollectionInput.setFilters(
                    collectionDef.getFilters().stream().map(
                            filter -> this.processFilterDefinition(filter, allFacetValues)
                    ).collect(Collectors.toList())
            );
            CollectionEntity collection = this.collectionService.create(ctx, createCollectionInput);
            collectionMap.put(collectionDef.getName(), collection);
        }

        // Wait for the created collection operations to complete before running the reindex of the search index.
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // TODO this.searchService.reindex(ctx);
    }

    private ConfigurableOperationInput processFilterDefinition(
            FacetValueCollectionFilterDefinition filter, List<FacetValueEntity> allFacetValueEntities) {
        switch (filter.getCode()) {
            case "facet-value-filter":
                List<Long> facetValueIds = filter.getFacetValueNames().stream()
                        .map(name -> allFacetValueEntities.stream().filter(fv -> Objects.equals(fv.getName(), name))
                                .findFirst().orElse(null))
                        .filter(facetValueEntity -> facetValueEntity != null)
                        .map(fv -> fv.getId()).collect(Collectors.toList());
                ConfigurableOperationInput input = new ConfigurableOperationInput();
                input.setCode(filter.getCode());

                ConfigArgInput argInput = new ConfigArgInput();
                argInput.setName("facetValueIds");
                try {
                    argInput.setValue(objectMapper.writeValueAsString(facetValueIds));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                input.getArguments().add(argInput);

                argInput = new ConfigArgInput();
                argInput.setName("containsAny");
                argInput.setValue(String.valueOf(filter.isContainsAny()));
                input.getArguments().add(argInput);

                return input;
            default:
                throw new RuntimeException("Filter with code " + filter.getCode() + " is not recognized");
        }
    }

    private RequestContext createRequestContext(InitialData data) {
        RequestContext ctx = new RequestContext();
        ctx.setApiType(ApiType.ADMIN);
        ctx.setAuthorized(true);
        ctx.setAuthorizedAsOwnerOnly(false);
        return ctx;
    }
}
