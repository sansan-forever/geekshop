/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.config.session_cache;

import co.jueyi.geekshop.types.common.Permission;
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
