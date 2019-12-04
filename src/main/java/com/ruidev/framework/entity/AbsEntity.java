package com.ruidev.framework.entity;

import java.io.Serializable;

/**
 * 可序列化的抽象实体类
 */
public class AbsEntity implements Serializable
{
	private static final long serialVersionUID = 1L;

	/**
	 * 该实体类必有获取id的方法
	 * @return
	 */
	public Long getId(){
    	return null;
    }
}
