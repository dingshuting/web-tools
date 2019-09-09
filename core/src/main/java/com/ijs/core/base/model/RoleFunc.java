package com.ijs.core.base.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * RoleFunction entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sys_role_func")
public class RoleFunc extends AbstractRoleFunc implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 7293701563773636590L;

	/** default constructor */
	public RoleFunc() {
	}

	/** full constructor */
	public RoleFunc(RoleFuncId id) {
		super(id);
	}

}
