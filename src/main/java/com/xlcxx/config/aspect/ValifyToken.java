package com.xlcxx.config.aspect;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Description: plodes
 * Created by yhsh on 2020/4/2 9:41
 * version 2.0
 * 方法说明
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValifyToken {
	boolean required() default true;
}
