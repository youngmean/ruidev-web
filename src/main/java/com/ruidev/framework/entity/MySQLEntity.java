package com.ruidev.framework.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 使用mysql数据库的基类
 */
@MappedSuperclass
public abstract class MySQLEntity extends AbsEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 公共id */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Long id;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

}
