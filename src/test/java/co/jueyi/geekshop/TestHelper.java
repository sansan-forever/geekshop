/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop;

import co.jueyi.geekshop.data_import.CollectionDefinition;
import co.jueyi.geekshop.data_import.FacetValueCollectionFilterDefinition;
import co.jueyi.geekshop.data_import.InitialData;
import co.jueyi.geekshop.data_import.ShippingMethodData;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created on Nov, 2020 by @author bobo
 */
public abstract class TestHelper {
    public static String getImportAssetsDir() {
        Path productsCsvPath = Paths.get("src","test", "resources", "fixtures", "assets");
        return productsCsvPath.toFile().getAbsolutePath();
    }

    public static String getTestFixture(String filename) {
        Path productsCsvPath = Paths.get("src","test", "resources", "fixtures", filename);
        return productsCsvPath.toFile().getAbsolutePath();
    }

    public static InitialData getInitialData() {
        InitialData initialData = new InitialData();

        ShippingMethodData shippingMethodData = new ShippingMethodData();
        shippingMethodData.setName("Standard Shipping");
        shippingMethodData.setPrice(500);
        initialData.getShippingMethods().add(shippingMethodData);

        shippingMethodData = new ShippingMethodData();
        shippingMethodData.setName("Express Shipping");
        shippingMethodData.setPrice(1000);
        initialData.getShippingMethods().add(shippingMethodData);

        CollectionDefinition collectionDef = new CollectionDefinition();
        collectionDef.setName("Plants");
        FacetValueCollectionFilterDefinition filterDef = new FacetValueCollectionFilterDefinition();
        filterDef.setCode("facet-value-filter");
        filterDef.getFacetValueNames().add("plants");
        filterDef.setContainsAny(false);
        collectionDef.getFilters().add(filterDef);
        initialData.getColletions().add(collectionDef);
        return initialData;
    }
}
