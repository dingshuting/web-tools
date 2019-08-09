package com.ijs.core.base.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * AbstractSysParameters entity provides the base persistence definition of the
 * SysParameters entity. @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractSysParameters implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 5263242418483058286L;
	private String id;
	private String name;
	private String summary;
	private String value;
	private Integer isLocked;
	private String spType;
	
	// Constructors

	/** default constructor */
	public AbstractSysParameters() {
	}

	/** minimal constructor */
	public AbstractSysParameters(String id) {
		this.id = id;
	}

	/** full constructor */
	public AbstractSysParameters(String id,String name,
			String summary, String value, Integer isLocked) {
		this.id = id;
		this.name = name;
		this.summary = summary;
		this.value = value;
		this.isLocked = isLocked;
	}

	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "name", length = 32)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "summary", length = 128)
	public String getSummary() {
		return this.summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	@Column(name = "value", length = 128)
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "is_locked")
	public Integer getIsLocked() {
		return this.isLocked;
	}

	public void setIsLocked(Integer isLocked) {
		this.isLocked = isLocked;
	}
	@Column(name = "sp_type")
	public String getSpType() {
		return spType;
	}

	public void setSpType(String spType) {
		this.spType = spType;
	}
	

}