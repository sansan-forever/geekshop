package co.jueyi.geekshop.custom.security;

import co.jueyi.geekshop.types.common.Permission;

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
public @interface RequiredPermission {
    Permission[] value() default {};
}

