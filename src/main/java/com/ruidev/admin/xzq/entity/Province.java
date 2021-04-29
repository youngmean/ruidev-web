package com.ruidev.admin.xzq.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.ruidev.framework.entity.CrudEntity;

/**
 * 省
 * 
 * @author 明
 * 
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "ruidev_xzq_province")
public class Province extends CrudEntity {

	private static final long serialVersionUID = 1L;
	
	private String code;
	private String name;
	@OneToMany(mappedBy = "province")
	private List<City> cities;

	/**
	 * 省份代码
	 * @return
	 */
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 省份名称
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<City> getCities() {
		return cities;
	}

	public void setCities(List<City> cities) {
		this.cities = cities;
	}

}
