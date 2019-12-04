package com.ruidev.admin.conf.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.ruidev.framework.entity.CrudEntity;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "ruidev_system_configuration")
public class Configuration extends CrudEntity {

	private static final long serialVersionUID = 1L;
	
	private String label;
	private String code;
	private String value;
	private String description;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
