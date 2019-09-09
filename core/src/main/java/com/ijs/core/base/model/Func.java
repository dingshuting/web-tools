package com.ijs.core.base.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ijs.core.base.Config;

/**
 * FunctionList entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sys_func")
@Cacheable
public class Func extends AbstractFunc implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 225445603026850336L;

	@Expose
    @SerializedName("children")
	private List<Func> subFuncs;
	
	/** default constructor */
	public Func() {
		subFuncs = new ArrayList<Func>();
	}
	
	public Func(String id) {	
		this.setId(id);
	}

	/** minimal constructor */
	public Func(String id, String parentId, String name) {
		super(id, parentId, name);
	}

	/** full constructor */
	public Func(String id, String parentId, String name, Integer level,
			Integer displayType, Integer types,String togo, Integer status,String icon) {
		super(id, parentId, name, level, displayType,types, togo, status,icon);
	}

	@Override
	public String toString() {		
		return Config.gson.toJson(this);
	}
	
	/**
	 * @return the subFuncs
	 */
	@Transient
	public List<Func> getSubFuncs() {
		return subFuncs;
	}

	/**
	 * @param subFuncs the subFuncs to set
	 */
	public void setSubFuncs(List<Func> subFuncs) {
		this.subFuncs = subFuncs;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		
		if (obj==null || !(obj instanceof Func)) {
			return false;
		}
		
		final Func func = (Func) obj;
		return this.getId()!=null?this.getId().equals(func.getId()):false;
	}
}
