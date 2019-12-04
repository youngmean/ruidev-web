package com.ruidev.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * web资源文件属性
 * 在调用 bo 的 save,saveOrUpdate 以及删除一个实体时,自动判断是否需要删除原文件
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WebResourceEntity {

	String[] fields();
}
