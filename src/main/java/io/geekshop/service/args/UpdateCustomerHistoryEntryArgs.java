/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.service.args;

import io.geekshop.common.RequestContext;
import io.geekshop.types.history.HistoryEntryType;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class UpdateCustomerHistoryEntryArgs {
    private Long entryId;
    private RequestContext ctx;
    private HistoryEntryType type;
    private Map<String, String> data = new HashMap<>();
}
