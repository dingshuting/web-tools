package com.ijs.core.base.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.google.gson.annotations.Expose;

/**
 * AbstractRegion entity provides the base persistence definition of the Region
 * entity. @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractRegion implements java.io.Serializable {

	// Fields
	@Expose
	private String id;
	private String parentId;
	@Expose
	private String name;
	private String remark;
	private String fullName;
	private Integer countryId;
	private String fullId;

	// Constructors

	/** default constructor */
	public AbstractRegion() {
	}

	/** minimal constructor */
	public AbstractRegion(String id) {
		this.id = id;
	}

	/** full constructor */
	public AbstractRegion(String id, String parentId, String name, String remark,String fullName,Integer countryId,String fullId) {
		this.id = id;
		this.parentId = parentId;
		this.name = name;
		this.remark = remark;
		this.fullName = fullName;
		this.countryId = countryId;
		this.fullId = fullId;
	}

	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 6)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "parent_id", length = 6)
	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Column(name = "name", length = 32)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "remark", length = 128)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(name = "full_name", length = 128)
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	@Column(name = "country_id")
	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}
	@Column(name = "full_id")
	public String getFullId() {
		return fullId;
	}

	public void setFullId(String fullId) {
		this.fullId = fullId;
	}

	
	
}