package com.ijs.core.base.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * RoleFunctionId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class RoleFuncId implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -6101748673377244L;
	private String funcId;
	private String rolId;

	// Constructors

	/** default constructor */
	public RoleFuncId() {
	}

	/** full constructor */
	public RoleFuncId(String funcId, String rolId) {
		this.funcId = funcId;
		this.rolId = rolId;
	}

	// Property accessors

	@Column(name = "Func_Id", length = 12)
	public String getFuncId() {
		return this.funcId;
	}

	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}

	@Column(name = "Role_ID")
	public String getRolId() {
		return this.rolId;
	}

	public void setRolId(String rolId) {
		this.rolId = rolId;
	}

	@Override
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof RoleFuncId))
			return false;
		RoleFuncId castOther = (RoleFuncId) other;

		return ((this.getFuncId() == castOther.getFuncId()) || (this
				.getFuncId() != null && castOther.getFuncId() != null && this
				.getFuncId().equals(castOther.getFuncId())))
				&& ((this.getRolId() == castOther.getRolId()) || (this
						.getRolId() != null && castOther.getRolId() != null && this
						.getRolId().equals(castOther.getRolId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getFuncId() == null ? 0 : this.getFuncId().hashCode());
		result = 37 * result
				+ (getRolId() == null ? 0 : this.getRolId().hashCode());
		return result;
	}

}