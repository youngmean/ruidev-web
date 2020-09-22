package com.ruidev.framework.user;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.ruidev.framework.entity.CrudEntity;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "ruidev_system_role")
public class RuidevRole extends CrudEntity {

	/**
	 * system role
	 */
	private static final long serialVersionUID = -201202101301L;
	@NotNull
	private String name;
	@NotNull
	private String code;
	private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
