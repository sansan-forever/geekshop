/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represent's a {@link co.jueyi.geekshop.types.customer.Customer}'s address.
 *
 * Created on Nov, 2020 by @author bobo
 */
@TableName(value = "tb_address")
@Data
@EqualsAndHashCode(callSuper = true)
public class AddressEntity extends BaseEntity {
    private Long customerId;
    private String fullName = "";
    private String company = "";
    private String streetLine1;
    private String streetLine2 = "";
    private String city = "";
    private String province = "";
    private String postalCode = "";
    private String phoneNumber = "";
    private Boolean defaultShippingAddress;
    private Boolean defaultBillingAddress;
}
