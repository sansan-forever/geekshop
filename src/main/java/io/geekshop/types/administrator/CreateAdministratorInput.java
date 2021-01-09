/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.administrator;

import io.geekshop.custom.validator.ValidPassword;
import lombok.Data;

import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class CreateAdministratorInput {
    private String firstName;
    private String lastName;
    @Email
    private String emailAddress;
    @ValidPassword
    private String password;
    private List<Long> roleIds = new ArrayList<>();
}
