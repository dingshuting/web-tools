package com.ijs.core.base.model;

// default package

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.ijs.core.base.model.Region;


/**
 * IsAddress entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "is_address")
public class Address extends AbstractIsAddress implements
		java.io.Serializable {
	private String regionName;
	
	// Constructors

	/** default constructor */
	public Address() {
	}

	/** minimal constructor */
	public Address(String id) {
		super(id);
	}

	/** full constructor */
	public Address(String id, String owner, String userId,
			String contactUserName, String region, String address, String zip,
			String phoneNo, Integer isDefault, Integer certType, String certCode,
			String cardfrontImg, String cardbackImg, Integer status,
			Integer isType) {
		super(id, owner, userId, contactUserName, region, address, zip,
				phoneNo, isDefault, certType, certCode, cardfrontImg,
				cardbackImg, status, isType);
	}
	
	public Address(String id, String owner, String userId,
			String contactUserName, String region, String address, String zip,
			String phoneNo, Integer isDefault, Integer certType, String certCode,
			String cardfrontImg, String cardbackImg, Integer status,
			Integer isType,String regionName) {
		super(id, owner, userId, contactUserName, region, address, zip,
				phoneNo, isDefault, certType, certCode, cardfrontImg,
				cardbackImg, status, isType);
		this.regionName = regionName;
	}

	
	@Transient
	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	@Transient
	public String getFullAddress() {
		return this.regionName+" "+this.getAddress();
	}

	
	
}
