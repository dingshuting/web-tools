package com.ijs.core.base.model;


import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

/**
 * AbstractExtraDataCols entity provides the base persistence definition of the
 * ExtraDataCols entity. @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractExtraDataCols extends BaseModel  implements java.io.Serializable {

	// Fields

	private String id;
	private String colCode;
	private String colValType;
	private String colName;
	private String extralDataId;
	private String validate;
	private String referenceExtralDataId;
	private Integer status;
	private Integer isSeach;
	private String options;
	private String style;
	private Integer length;
	private String desc;
	private Integer isShowInList;
	private String formClasses;
	private String listClasses;
	private String detailClasses;
	@OrderBy("ASC")
	private Integer colOrder;
	private Integer colType;
	private Integer isEdit;
	private String event;
	/**
	 * 列表的认证，用于根据不同角色的字段权限判断
	 */
	private List<ExtraDataColsAuthorization> colAuths;

	// Constructors

	/** default constructor */
	public AbstractExtraDataCols() {
	}

	/** minimal constructor */
	public AbstractExtraDataCols(String id) {
		this.id = id;
	}

	/** full constructor */
	public AbstractExtraDataCols(String id, String colCode, String colValType,
			String colName, String extralDataId, String validate,
			String referenceExtralDataId, Integer status, Integer isSeach,
			String options, String style, Integer length,
			String desc,Integer isShowInList,String formClasses,String listClasses,String detailClasses,Integer isEdit,String event) {
		this.id = id;
		this.colCode = colCode;
		this.colValType = colValType;
		this.colName = colName;
		this.extralDataId = extralDataId;
		this.validate = validate;
		this.referenceExtralDataId = referenceExtralDataId;
		this.status = status;
		this.isSeach = isSeach;
		this.options = options;
		this.style = style;
		this.length = length;
		this.desc = desc;
		this.isShowInList = isShowInList;
		this.formClasses = formClasses;
		this.listClasses = listClasses;
		this.detailClasses = detailClasses;
		this.isEdit=isEdit;
		this.event=event;
	}

	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 64)
	public String getId() {
		return this.id;
	}
	

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "col_code", length = 64)
	public String getColCode() {
		return this.colCode;
	}

	public void setColCode(String colCode) {
		this.colCode = colCode;
	}

	@Column(name = "col_val_type", length = 10)
	public String getColValType() {
		return this.colValType;
	}

	public void setColValType(String colValType) {
		this.colValType = colValType;
	}

	@Column(name = "col_name", length = 24)
	public String getColName() {
		return this.colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	@Column(name = "extral_data_id", length = 64)
	public String getExtralDataId() {
		return this.extralDataId;
	}

	public void setExtralDataId(String extralDataId) {
		this.extralDataId = extralDataId;
	}

	@Column(name = "validate")
	public String getValidate() {
		return this.validate;
	}

	public void setValidate(String validate) {
		this.validate = validate;
	}

	@Column(name = "reference_extral_data_id", length = 64)
	public String getReferenceExtralDataId() {
		return this.referenceExtralDataId;
	}

	public void setReferenceExtralDataId(String referenceExtralDataId) {
		this.referenceExtralDataId = referenceExtralDataId;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "is_seach")
	public Integer getIsSeach() {
		return this.isSeach;
	}

	public void setIsSeach(Integer isSeach) {
		this.isSeach = isSeach;
	}

	@Column(name = "options")
	public String getOptions() {
		return this.options;
	}

	public void setOptions(String options) {
		this.options = options;
	}


	@Column(name = "style")
	public String getStyle() {
		return this.style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	@Column(name = "length")
	public Integer getLength() {
		return this.length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	@Column(name = "col_desc")
	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	@Column(name = "is_show_in_list")
	public Integer getIsShowInList() {
		return isShowInList;
	}

	public void setIsShowInList(Integer isShowInList) {
		this.isShowInList = isShowInList;
	}
	@Column(name = "form_classes")
	public String getFormClasses() {
		return formClasses;
	}

	public void setFormClasses(String formClasses) {
		this.formClasses = formClasses;
	}
	@Column(name = "list_classes")
	public String getListClasses() {
		return listClasses;
	}

	public void setListClasses(String listClasses) {
		this.listClasses = listClasses;
	}
	@Column(name = "detail_classes")
	public String getDetailClasses() {
		return detailClasses;
	}

	public void setDetailClasses(String detailClasses) {
		this.detailClasses = detailClasses;
	}
	@Column(name = "col_order")
	public Integer getColOrder() {
		return colOrder;
	}

	public void setColOrder(Integer colOrder) {
		this.colOrder = colOrder;
	}
	@Column(name = "col_type")
	public Integer getColType() {
		return colType;
	}

	public void setColType(Integer colType) {
		this.colType = colType;
	}

	@Column(name = "is_edit")
	public Integer getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(Integer isEdit) {
		this.isEdit = isEdit;
	}
	@Column(name = "event")
	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}
	@Transient
	public List<ExtraDataColsAuthorization> getColAuths() {
		return colAuths;
	}

	public void setColAuths(List<ExtraDataColsAuthorization> colAuths) {
		this.colAuths = colAuths;
	}
	
}