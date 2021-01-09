/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.customer;

import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class UpdateCustomerInput {
    private Long id;
    private String title;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String emailAddress;
}
