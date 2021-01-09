/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.custom.security;

import io.geekshop.types.common.Permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 访问某个GraphQL mutation/query方法所需的权限
 *
 * Created on Nov, 2020 by @author bobo
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Allow {
    Permission[] value() default {};
}

