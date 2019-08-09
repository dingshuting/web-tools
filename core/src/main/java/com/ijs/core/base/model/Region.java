package com.ijs.core.base.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.gson.annotations.Expose;

/**
 * Region entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "region")
public class Region extends AbstractRegion implements java.io.Serializable {

	// Constructors
	@Expose
	private List<Region> childRegs;
	private List<Region> brotherRegs;
	private Region parentReg;
	/** default constructor */
	public Region() {
	}

	/** minimal constructor */
	public Region(String id) {
		super(id);
	}

	/** full constructor */
	public Region(String id, String parentId, String name, String remark,String fullName,Integer countryId,String fullId) {
		super(id, parentId, name, remark,fullName,countryId,fullId);
	}
	//查询构造函数
	public Region(String id, String parentId, String name, String remark,String fullName){
		this.setId(id);
		this.setParentId(parentId);
		this.setName(name);
		this.setRemark(remark);
		this.setFullName(fullName);
	}
	
	
	@Transient
	public List<Region> getChildRegs() {
		return childRegs;
	}

	public void setChildRegs(List<Region> childRegs) {
		this.childRegs = childRegs;
	}
	@Transient
	public Region getParentReg() {
		return parentReg;
	}

	public void setParentReg(Region parentReg) {
		this.parentReg = parentReg;
	}
	@Transient
	public List<Region> getBrotherRegs() {
		return brotherRegs;
	}

	public void setBrotherRegs(List<Region> brotherRegs) {
		this.brotherRegs = brotherRegs;
	}
	
	

}
