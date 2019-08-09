package com.ijs.core.base.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;

/**
 * AbstractUserRole entity provides the base persistence definition of the
 * UserRole entity. @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractUserRole implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -9192155012433269268L;
	private UserRoleId id;

	// Constructors

	/** default constructor */
	public AbstractUserRole() {
	}

	/** full constructor */
	public AbstractUserRole(UserRoleId id) {
		this.id = id;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "userId", column = @Column(name = "User_ID", nullable = false)),
			@AttributeOverride(name = "roleId", column = @Column(name = "Role_ID", nullable = false)) })
	public UserRoleId getId() {
		return this.id;
	}

	public void setId(UserRoleId id) {
		this.id = id;
	}

}