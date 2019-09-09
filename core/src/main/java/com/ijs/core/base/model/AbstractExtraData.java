package com.ijs.core.base.model;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * AbstractExtraData entity provides the base persistence definition of the
 * ExtraData entity. @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractExtraData extends BaseModel  implements java.io.Serializable {

	// Fields

	private String id;
	private String nameCodeAlias;
	private String nameCode;
	private String name;
	private Timestamp createTime;
	private String tableDesc;
	private Integer status;
	private String edType;
	private String formClasses;
	private String listClasses;
	private String detailClasses;
	private Integer isAuditing;
	private String modelName;
	// Constructors

	/** default constructor */
	public AbstractExtraData() {
	}

	/** minimal constructor */
	public AbstractExtraData(String id) {
		this.id = id;
	}

	/** full constructor */
	public AbstractExtraData(String id, String nameCodeAlias, String nameCode, String name, Timestamp createTime,
			String tableDesc, Integer status, String edType,String formClasses,String listClasses,String detailClasses,Integer isAuditing,String modelName) {
		this.id = id;
		this.nameCodeAlias = nameCodeAlias;
		this.nameCode = nameCode;
		this.name = name;
		this.createTime = createTime;
		this.tableDesc = tableDesc;
		this.status = status;
		this.edType = edType;
		this.formClasses = formClasses;
		this.listClasses = listClasses;
		this.detailClasses = detailClasses;
		this.isAuditing = isAuditing;
		this.modelName=modelName;
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

	@Column(name = "name_code_alias")
	public String getNameCodeAlias() {
		return this.nameCodeAlias;
	}

	public void setNameCodeAlias(String nameCodeAlias) {
		this.nameCodeAlias = nameCodeAlias;
	}

	@Column(name = "name_code", length = 54)
	public String getNameCode() {
		return this.nameCode;
	}

	public void setNameCode(String nameCode) {
		this.nameCode = nameCode;
	}

	@Column(name = "name", length = 54)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "create_time", length = 19)
	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Column(name = "table_desc")
	public String getTableDesc() {
		return tableDesc;
	}

	public void setTableDesc(String tableDesc) {
		this.tableDesc = tableDesc;
	}
	@Column(name = "status")
	public Integer getStatus() {
		return this.status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "ed_type")
	public String getEdType() {
		return this.edType;
	}

	public void setEdType(String edType) {
		this.edType = edType;
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

	@Column(name = "is_auditing")
	public Integer getIsAuditing() {
		return isAuditing;
	}

	public void setIsAuditing(Integer isAuditing) {
		this.isAuditing = isAuditing;
	}
	@Column(name = "model_name")
	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	
}