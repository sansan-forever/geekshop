/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.administrator;

import co.jueyi.geekshop.types.common.Node;
import co.jueyi.geekshop.types.user.User;
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
