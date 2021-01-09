/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.config.session_cache;

import io.geekshop.types.common.Permission;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * A simplified representation of the User associated with the current Session.
 *
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class CachedSessionUser {
    private Long id;
    private String identifier;
    private boolean verified;
    private List<Permission> permissions = new ArrayList<>();
}
