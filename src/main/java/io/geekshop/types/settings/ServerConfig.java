/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.types.settings;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class ServerConfig {
    private List<OrderProcessState> orderProcess = new ArrayList<>();
    private List<String> permittedAssetTypes = new ArrayList<>();
    private Map<String, String> customFields = new HashMap<>();
}
