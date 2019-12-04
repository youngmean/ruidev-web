package com.ruidev.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在action对应方法上添加该注解后,启用过滤查询
 *
 * @author	 	锐开科技 
 * @Copyright 	www.ruidev.com All rights reserved.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ActionEnableFilters {

}
