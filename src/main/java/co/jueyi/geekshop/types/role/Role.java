/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.role;

import co.jueyi.geekshop.types.common.Node;
import co.jueyi.geekshop.types.common.Permission;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class Role implements Node {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private String code;
    private String description;
    private List<Permission> permissions = new ArrayList<>();
}
