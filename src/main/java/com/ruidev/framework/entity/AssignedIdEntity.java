package com.ruidev.framework.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

@MappedSuperclass
public abstract class AssignedIdEntity extends AbsEntity {

	/**
	 * 自生成ID
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "dataid", strategy = "assigned")
	@GeneratedValue(generator = "dataid")
	@Column(name = "id", nullable = false)
	protected Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
