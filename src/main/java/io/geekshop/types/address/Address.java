/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.address;

import io.geekshop.types.common.Node;
import lombok.Data;

import java.util.Date;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class Address implements Node {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private String fullName;
    private String company;
    private String streetLine1;
    private String streetLine2;
    private String city;
    private String province;
    private String postalCode;
    private String phoneNumber;
    private Boolean defaultShippingAddress;
    private Boolean defaultBillingAddress;
}
