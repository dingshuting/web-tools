package com.ijs.core.base.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;

/**
 * AbstractRoleFunction entity provides the base persistence definition of the
 * RoleFunction entity. @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractRoleFunc extends BaseModel  implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 9127003237939940370L;
	private RoleFuncId id;

	// Constructors

	/** default constructor */
	public AbstractRoleFunc() {
	}

	/** full constructor */
	public AbstractRoleFunc(RoleFuncId id) {
		this.id = id;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "funcId", column = @Column(name = "Func_Id", length = 64)),
			@AttributeOverride(name = "rolId", column = @Column(name = "Role_ID")) })
	public RoleFuncId getId() {
		return this.id;
	}

	public void setId(RoleFuncId id) {
		this.id = id;
	}

}