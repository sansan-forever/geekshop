/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.administrator;

import io.geekshop.types.common.SortOrder;
import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class AdministratorSortParameter {
    private SortOrder id;
    private SortOrder createdAt;
    private SortOrder updatedAt;
    private SortOrder firstName;
    private SortOrder lastName;
    private SortOrder emailAddress;
}
