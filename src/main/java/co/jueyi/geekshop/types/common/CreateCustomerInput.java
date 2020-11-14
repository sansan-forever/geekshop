/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.common;

import co.jueyi.geekshop.custom.validator.PhoneNumber;
import lombok.Data;

import javax.validation.constraints.Email;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class CreateCustomerInput {
    private String title;
    private String firstName;
    private String lastName;
    @PhoneNumber
    private String phoneNumber;
    @Email
    private String emailAddress;
}
