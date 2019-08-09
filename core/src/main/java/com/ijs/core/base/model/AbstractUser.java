package com.ijs.core.base.model;

import com.google.gson.annotations.Expose;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OrderBy;

import java.sql.Timestamp;

/**
 * AbstractUser entity provides the base persistence definition of the User
 * entity. @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractUser implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5551293315396514458L;
	@Expose
	private String id;
	@Expose
	private String owner;
	@Expose
	private String name;
	@Expose
	private String accountNo;
	private String password;
	@Expose
	private String orgId;
	@Expose
	private String title;
	@Expose
	private String email;
	@Expose
	private String mobilePhone;
	@Expose
	private String officePhone;
	@Expose
	@OrderBy("desc")
	@FieldInterface(value = "regTime DESC")
	private Timestamp regTime;
	@Expose
	private Integer status;
	private String sign;
	@Expose
	private String headCover;
	@Expose
	private String sysDepId;

	// 新加user表字段
	@Expose
	private String nation;
	@Expose
	private Timestamp birthday;
	@Expose
	private String education;
	@Expose
	private String QQ;
	@Expose
	private String idNumber;
	@Expose
	private String zipCode; // 邮编
	@Expose
	private String region;
	@Expose
	private String address;
	@Expose
	private Timestamp createTime;
	@Expose
	private Integer userType;
	@Expose
	private Integer passType;
	@Expose
	private String sex;
	@Expose
	private Integer titleType;


	@Column(name = "nation")
	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	@Column(name = "birthday")
	public Timestamp getBirthday() {
		return birthday;
	}

	public void setBirthday(Timestamp birthday) {
		this.birthday = birthday;
	}

	@Column(name = "education")
	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	@Column(name = "QQ")
	public String getQQ() {
		return QQ;
	}

	public void setQQ(String qQ) {
		QQ = qQ;
	}

	@Column(name = "idNumber")
	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	@Column(name = "zipCode")
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Column(name = "region")
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Column(name = "address")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	// Constructors

	/** default constructor */
	public AbstractUser() {
	}

	/** minimal constructor */
	public AbstractUser(Timestamp regTime) {
		this.regTime = regTime;
	}

	/** full constructor */
	public AbstractUser(String owner, String name, String accountNo,
			String password, String orgId, String title, String email,
			String mobilePhone, String officePhone, Timestamp regTime,
			Integer status, String sign) {
		this.owner = owner;
		this.name = name;
		this.accountNo = accountNo;
		this.password = password;
		this.orgId = orgId;
		this.title = title;
		this.email = email;
		this.mobilePhone = mobilePhone;
		this.officePhone = officePhone;
		this.regTime = regTime;
		this.status = status;
		this.sign = sign;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "owner")
	public String getOwner() {
		if(this.owner==null) {
			return "";
		}
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Column(name = "name", length = 64)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "account_NO", unique = true, length = 64)
	public String getAccountNo() {
		return this.accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	@Column(name = "password", length = 64)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "Org_Id")
	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	@Column(name = "title", length = 64)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "email", length = 64)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "Mobile_Phone", length = 16)
	public String getMobilePhone() {
		return this.mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	@Column(name = "Office_Phone", length = 16)
	public String getOfficePhone() {
		return this.officePhone==null?"":this.officePhone;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	@Column(name = "Reg_Time", nullable = false, length = 19)
	public Timestamp getRegTime() {
		return this.regTime;
	}

	public void setRegTime(Timestamp regTime) {
		this.regTime = regTime;
	}

	@Column(name = "Status")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "Sign", length = 64)
	public String getSign() {
		return this.sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@Column(name = "head_cover")
	public String getHeadCover() {
		return headCover;
	}

	public void setHeadCover(String headCover) {
		this.headCover = headCover;
	}

	@Column(name = "create_time", nullable = false, length = 19)
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Column(name = "user_type")
	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	@Column(name = "pass_type")
	public Integer getPassType() {
		return passType;
	}

	public void setPassType(Integer passType) {
		this.passType = passType;
	}

	@Column(name = "sex")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Column(name = "title_type")
	public Integer getTitleType() {
		return titleType;
	}

	public void setTitleType(Integer titleType) {
		this.titleType = titleType;
	}
	@Column(name = "dep_type")
	public String getSysDepId() {
		return sysDepId;
	}

	public void setSysDepId(String sysDepId) {
		this.sysDepId = sysDepId;
	}
	
	
	
}