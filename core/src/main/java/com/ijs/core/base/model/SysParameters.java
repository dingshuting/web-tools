package com.ijs.core.base.model;

import java.util.Properties;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * SysParameters entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sys_parameters")

public class SysParameters extends AbstractSysParameters implements
		java.io.Serializable {
	private String typeName;
	public static final Properties pps = new Properties();
	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = -6207440048598825273L;

	/** default constructor */
	public SysParameters() {
	}
   public SysParameters(String id, String name,String summary,String value1,Integer isLocked){
	   super(id,name,summary,value1,isLocked);
   }
	public SysParameters(String id) {
		setId(id);
	}

}
