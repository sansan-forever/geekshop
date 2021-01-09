/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.service.helpers.input;

import io.geekshop.entity.RoleEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class CreateAdministratorAndUserInput {
    private String strategy;
    private String externalIdentifier;
    private String identifier;
    private String emailAddress;
    private String firstName;
    private String lastName;
    private List<RoleEntity> roles = new ArrayList<>();
}
