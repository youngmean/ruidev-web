package com.ruidev.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在bean上添加该注解后,该bean(即需加上@Service或@Component注解)即可扩展框架api<br>
 * 参数index: 用来控制多个判断控制器时执行判断的顺序
 *
 * @author	 	锐开科技 
 * @Copyright 	www.ruidev.com All rights reserved.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ApiSort {
	
	int index() default 0;
	
}
