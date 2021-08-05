package com.ruidev.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在bean上添加该注解后,该bean(即需加上@Service或@Component注解)即可扩展框架api<br>
 * 参数name: 用来定义api的名称
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ApiName {
	
	String name() default "_";
	
}
