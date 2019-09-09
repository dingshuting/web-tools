package com.ijs.core.base.model;


import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * ExtraDataCols entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "extra_data_cols")
@Cacheable
public class ExtraDataCols extends AbstractExtraDataCols implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public ExtraDataCols() {
	}

	/** minimal constructor */
	public ExtraDataCols(String id) {
		super(id);
	}

	/** full constructor */
	public ExtraDataCols(String id, String colCode, String colValType,
			String colName, String extralDataId, String validate,
			String referenceExtralDataId, Integer status, Integer isSeach,
			String options, String style, Integer length,
			String desc,Integer isShowInList,String formClasses,String listClasses,String detailClasses,Integer isEdit,String event) {
		super(id, colCode, colValType, colName, extralDataId, validate,
				referenceExtralDataId, status, isSeach, options,
				style, length, desc,isShowInList,formClasses,listClasses,detailClasses,isEdit,event);
	}

	@Transient
	public String getRules() {
		
		return getValidate();
	}
	@Transient
	public String getColCn() {
		// TODO Auto-generated method stub
		return getColName();
	}
	private List<ExtraDataColsAuthorization> colsAuthorizations;

	@Transient
	public List<ExtraDataColsAuthorization> getColsAuthorizations() {
		return colsAuthorizations;
	}

	public void setColsAuthorizations(List<ExtraDataColsAuthorization> colsAuthorizations) {
		this.colsAuthorizations = colsAuthorizations;
	}
}
