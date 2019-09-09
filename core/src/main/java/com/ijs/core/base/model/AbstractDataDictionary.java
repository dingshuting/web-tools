package com.ijs.core.base.model;


import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * AbstractBuyDataDictionary entity provides the base persistence definition of
 * the BuyDataDictionary entity. @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractDataDictionary extends BaseModel  implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1176710128024278112L;
	private String id;
	private String pid;
	private String name;
	private String remark;
	private Integer locked;
	private Integer status;
	
	// Constructors

	/** default constructor */
	public AbstractDataDictionary() {
	}

	/** full constructor */
	public AbstractDataDictionary(String id, String name,
			String remark,Integer locked, Integer status) {
		this.id = id;
		this.name = name;
		this.remark = remark;
		this.locked = locked;
		this.status = status;
		
	}

	

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	@Column(name = "Types", unique = true, nullable = false)
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	@Column(name = "Name", length = 64)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "Remark", length = 512)
	public String getRemark() {
		return this.remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}

	
	@Column(name = "locked")
	public Integer getLocked() {
		return locked;
	}

	public void setLocked(Integer locked) {
		this.locked = locked;
	}

	@Column(name = "Status")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	

}