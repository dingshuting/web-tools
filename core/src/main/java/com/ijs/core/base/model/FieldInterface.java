package com.ijs.core.base.model;



import java.lang.annotation.Documented;

import java.lang.annotation.ElementType;

import java.lang.annotation.Retention;

import java.lang.annotation.RetentionPolicy;

import java.lang.annotation.Target;


@Documented
@Target({ElementType.FIELD}) //注解应用类型
@Retention(RetentionPolicy.RUNTIME) // 注解的类型
public @interface FieldInterface {
	String name() default "";
	String value() default "";

}
