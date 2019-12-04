package com.ruidev.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 可作逻辑删除的实体,实体类在添加该注解后,数据在被删除时默认只修改标识位
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LogicalDeletableEntity {

}
