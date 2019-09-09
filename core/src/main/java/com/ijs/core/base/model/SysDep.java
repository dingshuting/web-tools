package com.ijs.core.base.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "sys_dep")
public class SysDep extends AbstractSysDep implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 506514966091773715L;
	//保存该部门下的下级角色信息
	private List<Role> children;
	public SysDep() {
		super();
	}

	public SysDep(String id, String name) {
		super(id, name);
	}
	@Transient
	public List<Role> getChildren() {
		return children;
	}

	public void setChildren(List<Role> children) {
		this.children = children;
	}
	
}
