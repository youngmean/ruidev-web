package com.ruidev.framework.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

/**
 * 基本Entity类, 包含了<br>
 * 	create_by:	创建者ID<br>
 *  update_by:	更新者ID<br>
 *  create_date:创建时间<br>
 *  update_date:更新时间<br>
 *  data_flag:	是否有效(1:有效,0:无效)<br>
 *  五个基本的字段,
 *
 * @author	 	锐开科技 
 * @Copyright 	www.ruidev.com All rights reserved.
 */
@MappedSuperclass
@FilterDefs({@FilterDef(name="dataFilter", parameters=@ParamDef(name="createBy", type="long")),
	@FilterDef(name="logicaldelFilter", parameters=@ParamDef(name="dataFlag", type="string"))})
@Filters({@Filter(name="dataFilter", condition="create_by=:createBy"), 
	@Filter(name="logicaldelFilter", condition="data_flag=:dataFlag")})
public abstract class CrudEntity extends MySQLEntity {

	private static final long serialVersionUID = 1L;
	/** 创建者 */
	@Column(name = "create_by", updatable = false)
	protected Long createBy;
	/**
	 * 数据状态
	 */
	@Column(name = "data_flag", length = 1)
	protected String dataFlag;

	/** 更新者 */
	@Column(name = "update_by")
	protected Long updateBy;

	/** 创建日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date", updatable = false)
	protected Date createDate;

	/** 更新日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_date")
	protected Date updateDate;

	/**
	 * @return Returns the createBy.
	 */
	public Long getCreateBy() {
		return createBy;
	}

	/**
	 * @param createBy
	 *            The createBy to set.
	 */
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	/**
	 * @return Returns the createDate.
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate
	 *            The createDate to set.
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return Returns the updateBy.
	 */
	public Long getUpdateBy() {
		return updateBy;
	}

	/**
	 * @param updateBy
	 *            The updateBy to set.
	 */
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	/**
	 * @return Returns the updateDate.
	 */
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate
	 *            The updateDate to set.
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getDataFlag() {
		return dataFlag;
	}

	public void setDataFlag(String dataFlag) {
		this.dataFlag = dataFlag;
	}

}
