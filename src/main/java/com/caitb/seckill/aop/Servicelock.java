package com.caitb.seckill.aop;

import java.lang.annotation.*;

/**
 * 自定义注解 同步锁
 *
 * @作者 cai_tb
 * @创建时间 2018/7/15
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Servicelock {

    String description()  default "";
}
