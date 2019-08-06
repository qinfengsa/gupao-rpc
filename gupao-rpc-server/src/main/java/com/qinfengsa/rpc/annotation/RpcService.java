package com.qinfengsa.rpc.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Rpc服务注解
 * @author: qinfengsa
 * @date: 2019/7/25 15:24
 */
@Target(ElementType.TYPE) //类/接口
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RpcService {

    /**
     * 类名
     * @return
     */
    Class<?> value();


    /**
     * 版本号
     */
    String version() default "";
}
