package com.ijs.core.base.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * UserRole entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sys_user_role")
public class UserRole extends AbstractUserRole implements java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = -1708360124157973010L;

	/** default constructor */
	public UserRole() {
	}

	/** full constructor */
	public UserRole(UserRoleId id) {
		super(id);
	}

}
