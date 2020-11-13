/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.custom.graphql;

import co.jueyi.geekshop.exception.InternalServerError;
import graphql.GraphQLError;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 统一业务异常处理切面
 *
 * Created on Nov, 2020 by @author bobo
 */
@Aspect
@Component
@Order(3) // 安全校验和参数校验之后执行
@Slf4j
public class GraphQLErrorAspect {
    /**
     * 统一包装业务逻辑处理异常
     */
    @Around("allGraphQLResolverMethods() && isDefinedInApplication()")
    public Object validate(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            if (throwable instanceof GraphQLError) throw throwable;
            log.error("Error to process resolver logic!!!", throwable);
            throw new InternalServerError();
        }
    }

    /**
     * Matches all beans that implement {@link graphql.kickstart.tools.GraphQLResolver}
     * note: {@code GraphQLMutationResolver}, {@code GraphQLQueryResolver} etc
     * extend base GraphQLResolver interface
     */
    @Pointcut("target(graphql.kickstart.tools.GraphQLResolver)")
    private void allGraphQLResolverMethods() {
    }

    /**
     * Matches all beans in io.geekshop package
     * resolvers must be in this package (subpackages)
     */
    @Pointcut("within(co.jueyi.geekshop..*)")
    private void isDefinedInApplication() {
    }
}
