package com.itways.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;

import com.itways.common.CommonConfiguration;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableCaching
@Import(CommonConfiguration.class)
public @interface EnableItwaysCommon {

}
