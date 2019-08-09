package com.ijs.core.base.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * UserRoleId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class UserRoleId implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 7215273581930746716L;
	private Integer userId;
	private Integer roleId;

	// Constructors

	/** default constructor */
	public UserRoleId() {
	}

	/** full constructor */
	public UserRoleId(Integer userId, Integer roleId) {
		this.userId = userId;
		this.roleId = roleId;
	}

	// Property accessors

	@Column(name = "User_ID", nullable = false)
	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name = "Role_ID", nullable = false)
	public Integer getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	@Override
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof UserRoleId))
			return false;
		UserRoleId castOther = (UserRoleId) other;

		return ((this.getUserId() == castOther.getUserId()) || (this
				.getUserId() != null && castOther.getUserId() != null && this
				.getUserId().equals(castOther.getUserId())))
				&& ((this.getRoleId() == castOther.getRoleId()) || (this
						.getRoleId() != null && castOther.getRoleId() != null && this
						.getRoleId().equals(castOther.getRoleId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getUserId() == null ? 0 : this.getUserId().hashCode());
		result = 37 * result
				+ (getRoleId() == null ? 0 : this.getRoleId().hashCode());
		return result;
	}
}