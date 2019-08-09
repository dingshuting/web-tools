package com.ijs.core.base.model;

import java.util.Date;

// default package

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OrderBy;

/**
 * AbstractIsAddress entity provides the base persistence definition of the
 * IsAddress entity. @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractIsAddress implements java.io.Serializable {

	// Fields

	private String id;
	private String owner;
	private String userId;
	private String contactUserName;
	private String region;
	private String address;
	private String zip;
	private String phoneNo;
	@OrderBy("asc")
	@FieldInterface(value = "isDefault ASC")
	private Integer isDefault;
	private Integer certType;
	private String certCode;
	private String cardfrontImg;
	private String cardbackImg;
	private Integer status;
	private Integer isType;
	private Date createTime;

	// Constructors

	/** default constructor */
	public AbstractIsAddress() {
	}

	/** minimal constructor */
	public AbstractIsAddress(String id) {
		this.id = id;
	}

	/** full constructor */
	public AbstractIsAddress(String id, String owner, String userId,
			String contactUserName, String region, String address, String zip,
			String phoneNo, Integer isDefault, Integer certType, String certCode,
			String cardfrontImg, String cardbackImg, Integer status,
			Integer isType) {
		this.id = id;
		this.owner = owner;
		this.userId = userId;
		this.contactUserName = contactUserName;
		this.region = region;
		this.address = address;
		this.zip = zip;
		this.phoneNo = phoneNo;
		this.isDefault = isDefault;
		this.certType = certType;
		this.certCode = certCode;
		this.cardfrontImg = cardfrontImg;
		this.cardbackImg = cardbackImg;
		this.status = status;
		this.isType = isType;
	}

	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 54)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "owner", length = 54)
	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Column(name = "user_id", length = 54)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "contact_user_name", length = 64)
	public String getContactUserName() {
		return this.contactUserName;
	}

	public void setContactUserName(String contactUserName) {
		this.contactUserName = contactUserName;
	}

	@Column(name = "region")
	public String getRegion() {
		return this.region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Column(name = "address", length = 128)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "zip", length = 32)
	public String getZip() {
		return this.zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	@Column(name = "phone_no", length = 32)
	public String getPhoneNo() {
		return this.phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	@Column(name = "is_default")
	public Integer getIsDefault() {
		return this.isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	@Column(name = "cert_type")
	public Integer getCertType() {
		return this.certType;
	}

	public void setCertType(Integer certType) {
		this.certType = certType;
	}

	@Column(name = "cert_code", length = 20)
	public String getCertCode() {
		return this.certCode;
	}

	public void setCertCode(String certCode) {
		this.certCode = certCode;
	}

	@Column(name = "cardfront_img", length = 128)
	public String getCardfrontImg() {
		return this.cardfrontImg;
	}

	public void setCardfrontImg(String cardfrontImg) {
		this.cardfrontImg = cardfrontImg;
	}

	@Column(name = "cardback_img", length = 128)
	public String getCardbackImg() {
		return this.cardbackImg;
	}

	public void setCardbackImg(String cardbackImg) {
		this.cardbackImg = cardbackImg;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "is_type")
	public Integer getIsType() {
		return this.isType;
	}

	public void setIsType(Integer isType) {
		this.isType = isType;
	}
	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}