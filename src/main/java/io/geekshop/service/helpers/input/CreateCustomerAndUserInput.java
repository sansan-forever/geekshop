/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.service.helpers.input;

import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class CreateCustomerAndUserInput {
    private String strategy;
    private String externalIdentifier;
    private Boolean verified;
    private String emailAddress;
    private String firstName;
    private String lastName;
}
