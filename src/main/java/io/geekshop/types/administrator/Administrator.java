/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.types.administrator;

import io.geekshop.types.common.Node;
import io.geekshop.types.user.User;
import lombok.Data;

import java.util.Date;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class Administrator implements Node {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private User user;
}
