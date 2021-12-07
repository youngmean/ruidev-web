package com.ruidev.admin.xzq.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.ruidev.admin.xzq.vo.DistrictData;
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

	public DistrictData toDistrictData() {
		DistrictData data = new DistrictData();
		data.setLabel(name);
		data.setValue(code);
		if(cities != null && cities.size() > 0) {
			List<DistrictData> children = new ArrayList<DistrictData>();
			for(City c : cities) {
				children.add(c.toDistrictData());
			}
			data.setChildren(children);
		}
		return data;
	}
}
