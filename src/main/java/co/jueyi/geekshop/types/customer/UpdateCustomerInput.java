/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.customer;

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
