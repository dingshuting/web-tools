package com.ijs.core.base.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.google.gson.annotations.Expose;

/**
 * AbstractRole entity provides the base persistence definition of the Role
 * entity. @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractRole implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -6399934587209017984L;
	@Expose
	private String id;
	@Expose
	private String name;
	private Integer types;
	private String remark;
	private String code;
	private Integer applyToSystem;
	private String owner;
	private Integer orgId;
	@Expose
	private List<Func> funcs;
	private String parentId;
	private Integer status;
	// Constructors
	/** default constructor */
	public AbstractRole() {
	}

	/** full constructor */
	public AbstractRole(String id, String name, Integer types, String remark,
			String code, Integer applyToSystem, String owner, Integer orgId,String parentId, Integer status) {
		this.id = id;
		this.name = name;
		this.types = types;
		this.remark = remark;
		this.code = code;
		this.applyToSystem = applyToSystem;
		this.owner = owner;
		this.orgId = orgId;
		this.parentId = parentId;
		this.status = status;
	}
    
	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false)
	public String getId() {
		return this.id;
	}


	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "Name", length = 32)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "Types", updatable=false)
	public Integer getTypes() {
		return this.types;
	}

	public void setTypes(Integer types) {
		this.types = types;
	}

	@Column(name = "Remark", length = 256)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "Code", length = 16, updatable=false)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the applyToSystem
	 */
	@Column(name = "Apply_To_System", updatable=false)
	public Integer getApplyToSystem() {
		return applyToSystem;
	}

	/**
	 * @param applyToSystem the applyToSystem to set
	 */
	public void setApplyToSystem(Integer applyToSystem) {
		this.applyToSystem = applyToSystem;
	}
	@Transient
	/**
	 * @return the funcs
	 */
	public List<Func> getFuncs() {
		if(funcs == null){
			funcs = new ArrayList<Func>();
		}
		return funcs;
	}

	/**
	 * @param funcs the funcs to set
	 */
	public void setFuncs(List<Func> funcs) {
		this.funcs = funcs;
	}
	@Column(name = "owner")
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	@Column(name = "Org_Id")
	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	@Column(name = "parent_id")
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	@Column(name = "Status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
}