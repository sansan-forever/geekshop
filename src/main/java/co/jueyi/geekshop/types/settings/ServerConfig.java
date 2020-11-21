/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.settings;

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
