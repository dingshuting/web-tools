package com.ijs.core.base.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * BuyDataDictionary entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sys_data_dictionary")
@Cacheable
public class DataDictionary extends AbstractDataDictionary implements java.io.Serializable {

	// Constructors
	/**
	 * 
	 */
	private static final long serialVersionUID = 3839834524381949130L;

	/** default constructor */
	public DataDictionary() {
	}

}
