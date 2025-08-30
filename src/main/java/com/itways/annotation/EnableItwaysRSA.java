package com.itways.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import com.itways.common.RsaKeysConfig;
import com.itways.common.props.JwtProperties;

/**
 * @author SSATWA
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@PropertySource("classpath:application-common.properties")
@Import(value = { JwtProperties.class, RsaKeysConfig.class })
public @interface EnableItwaysRSA {

}
