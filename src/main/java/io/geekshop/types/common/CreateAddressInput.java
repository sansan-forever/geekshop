/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.common;

import io.geekshop.custom.validator.PhoneNumber;
import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class CreateAddressInput {
    private String fullName;
    private String company;
    private String streetLine1;
    private String streetLine2;
    private String city;
    private String province;
    private String postalCode;
    @PhoneNumber
    private String phoneNumber;
    private Boolean defaultShippingAddress;
    private Boolean defaultBillingAddress;
}
