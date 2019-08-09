package com.ijs.core.base.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * AbstractFunctionList entity provides the base persistence definition of the
 * FunctionList entity. @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractFunc implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -649894542810579594L;
	@Expose
	private String id;
	@Expose
    @SerializedName("pId") 
	private String parentId;
	@Expose
    @SerializedName("name") 
	private String name;
	private Integer level;
	private Integer displayType;
	private Integer types;
	private String togo;
	private Integer status;
	private String icon;
	private Integer fnOr;
	private String fnDesc;
	private String url;
	@Expose
    @SerializedName("extraDataId") 
	private String extraDataId;
	private Date createTime;
	private String modelClass;
	// Constructors

	/** default constructor */
	public AbstractFunc() {
	}

	/** minimal constructor */
	public AbstractFunc(String id, String parentId, String name) {
		this.id = id;
		this.parentId = parentId;
		this.name = name;
	}

	/** full constructor */
	public AbstractFunc(String id, String parentId, String name,
			Integer level, Integer displayType,Integer types, String togo, Integer status,String icon) {
		this.id = id;
		this.parentId = parentId;
		this.name = name;
		this.level = level;
		this.displayType = displayType;
		this.togo = togo;
		this.status = status;
		this.icon = icon;
		this.types=types;
	}

	// Property accessors
	@Id
	@Column(name = "Id", unique = true, nullable = false, length = 64)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "Parent_id", nullable = false, length = 64)
	
	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Column(name = "Name", nullable = false, length = 32)
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "Level")
	
	public Integer getLevel() {
		return this.level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@Column(name = "Display_Type")
	
	public Integer getDisplayType() {
		return this.displayType;
	}

	public void setDisplayType(Integer displayType) {
		this.displayType = displayType;
	}

	@Column(name = "Togo", length = 128)
	
	public String getTogo() {
		return this.togo;
	}

	public void setTogo(String togo) {
		this.togo = togo;
	}

	@Column(name = "Status")
	
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the types
	 */
	@Column(name = "types")
	
	public Integer getTypes() {
		return types;
	}

	/**
	 * @param types the types to set
	 */
	public void setTypes(Integer types) {
		this.types = types;
	}
	
	@Column(name = "icon")
	
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	@Column(name = "fn_or")
	public Integer getFnOr() {
		return fnOr;
	}

	public void setFnOr(Integer fnOr) {
		this.fnOr = fnOr;
	}
	@Column(name = "fn_desc")
	public String getFnDesc() {
		return fnDesc;
	}

	public void setFnDesc(String fnDesc) {
		this.fnDesc = fnDesc;
	}
	@Column(name = "url")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	@Column(name = "extra_data_id")
	public String getExtraDataId() {
		return extraDataId;
	}

	public void setExtraDataId(String extraDataId) {
		this.extraDataId = extraDataId;
	}
	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Column(name = "model_class")
	public String getModelClass() {
		return modelClass;
	}

	public void setModelClass(String modelClass) {
		this.modelClass = modelClass;
	}
	
	
	
}