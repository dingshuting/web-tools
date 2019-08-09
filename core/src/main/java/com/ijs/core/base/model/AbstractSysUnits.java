package com.ijs.core.base.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * AbstractSysUnits entity provides the base persistence definition of the
 * SysUnits entity. @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractSysUnits implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7827485828342484322L;
	// Fields

	private String code;
	private String name;
	private String pinyin;
	private Integer status;

	// Constructors

	/** default constructor */
	public AbstractSysUnits() {
	}

	/** minimal constructor */
	public AbstractSysUnits(String code) {
		this.code = code;
	}

	/** full constructor */
	public AbstractSysUnits(String code, String name, String pinyin,
			Integer status) {
		this.code = code;
		this.name = name;
		this.pinyin = pinyin;
		this.status = status;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "code", unique = true, nullable = false, length = 8)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "name", length = 8)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "pinyin", length = 32)
	public String getPinyin() {
		return this.pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}