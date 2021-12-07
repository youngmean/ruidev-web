package com.ruidev.admin.xzq.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.ruidev.admin.xzq.vo.DistrictData;
import com.ruidev.framework.entity.CrudEntity;

/**
 * 区县
 * 
 * @author 明
 * 
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "ruidev_xzq_area")
public class Area extends CrudEntity {

	private static final long serialVersionUID = -3061332150949367098L;
	private String code;
	private String name;
	@Column(name = "city_code")
	private String cityCode;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "city_code", referencedColumnName = "code", insertable = false, updatable = false)
	private City city;

	/**
	 * 区代码
	 * @return
	 */
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 区名称
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 所在市代码
	 * @return
	 */
	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	/**
	 * 所在市
	 * @return
	 */
	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public DistrictData toDistrictData() {
		DistrictData data = new DistrictData();
		data.setLabel(name);
		data.setValue(code);
		return data;
	}
}
