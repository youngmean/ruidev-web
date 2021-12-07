package com.ruidev.admin.xzq.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.ruidev.admin.xzq.vo.DistrictData;
import com.ruidev.framework.entity.CrudEntity;

/**
 * 市
 * @author 明
 *
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "ruidev_xzq_city")
public class City extends CrudEntity {

	private static final long serialVersionUID = 3767960016037184331L;
	private String code;
	private String name;
	@Column(name = "province_code")
	private String provinceCode;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "province_code", referencedColumnName = "code", insertable = false, updatable = false)
	private Province province;
	@OneToMany(mappedBy = "city", fetch = FetchType.LAZY)
	private List<Area> areas;

	/**
	 * 城市代码
	 * @return
	 */
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 城市名称
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 所在省份代码
	 * @return
	 */
	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	/**
	 * 所在省份
	 * @return
	 */
	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public List<Area> getAreas() {
		return areas;
	}

	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}

	public DistrictData toDistrictData() {
		DistrictData data = new DistrictData();
		data.setLabel(name);
		data.setValue(code);
		if(areas != null && areas.size() > 0) {
			List<DistrictData> children = new ArrayList<DistrictData>();
			for(Area c : areas) {
				children.add(c.toDistrictData());
			}
			data.setChildren(children);
		}
		return data;
	}
}
