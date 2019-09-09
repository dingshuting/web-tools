package com.ijs.core.base.model;


import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * ExtraData entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "extra_data")
@Cacheable
public class ExtraData extends AbstractExtraData implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public ExtraData() {
	}

	/** minimal constructor */
	public ExtraData(String id) {
		super(id);
	}
	
	/** full constructor */
	public ExtraData(String id, String nameCodeAlias, String nameCode,
			String name, Timestamp createTime, String desc, Integer status,
			String edType,String formClasses,String listClasses,String detailClasses,Integer isAuditing,String modelName,String service) {
		super(id, nameCodeAlias, nameCode, name, createTime, desc, status,
				edType,formClasses,listClasses,detailClasses,isAuditing,modelName);
	}
	private List<ExtraDataCols> cols;
	
	@Transient
	public List<ExtraDataCols> getCols() {
		return cols;
	}

	public void setCols(List<ExtraDataCols> cols) {
		this.cols = cols;
	}
}
