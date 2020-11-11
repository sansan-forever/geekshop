/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.service.helper.input;

import co.jueyi.geekshop.entity.RoleEntity;
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
