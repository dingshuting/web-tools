package com.ijs.core.base.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * SysUnits entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sys_units")
public class SysUnits extends AbstractSysUnits implements java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = -776498714641876974L;

	/** default constructor */
	public SysUnits() {
	}

	/** minimal constructor */
	public SysUnits(String code) {
		super(code);
	}

	/** full constructor */
	public SysUnits(String code, String name, String pinyin, Integer status) {
		super(code, name, pinyin, status);
	}

}
