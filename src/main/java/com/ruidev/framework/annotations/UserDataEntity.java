package com.ruidev.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用户数据相关的基类,实体类在添加该注解后,在加载数据时只能维护该用户的数据
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UserDataEntity {
	
	String field() default "createBy";
	
}
