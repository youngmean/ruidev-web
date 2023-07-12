package com.ruidev.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在action对应方法上添加该注解后,无需登录即可请求
 *
 * @author 		锐开科技 
 * @Copyright 	www.ruidev.com All rights reserved.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ActionEnableAnon {
	
	String[] methods() default {"get", "post"};
	String dataType() default "json";
	String[] validateSignMethods() default {"get", "post"};
	boolean skipSign() default false;
	
}

	