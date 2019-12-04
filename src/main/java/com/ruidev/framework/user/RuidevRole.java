package com.ruidev.framework.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.ruidev.framework.constant.RoleLevel;
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
	@Column
	@NotNull
	private String name;
	@Column
	private String description;

	/*
	 * @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch =
	 * FetchType.LAZY, mappedBy = "roles") private List<RuidevUser> users;
	 */
	@Column(name = "role_level")
	private Integer roleLevel = RoleLevel.ROLE_ADMIN.getLevel();

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

	public Integer getRoleLevel() {
		return roleLevel;
	}

	public void setRoleLevel(Integer roleLevel) {
		this.roleLevel = roleLevel;
	}
}
