package com.ijs.core.base.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OrderBy;

/**
 * 部门类
 * @author issuser
 *
 */
@MappedSuperclass
public abstract class AbstractSysDep implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5873330127154025679L;
    private String id;
    private String name;
    private String owner;
    private String leaderId;
    private String memo;
    private Integer status;
    private String province;
    private String city;
    private String address;
    @OrderBy("DESC")
    @FieldInterface(value = "createTime DESC")
    private Timestamp createTime;
	public AbstractSysDep() {
	}
	public AbstractSysDep(String id, String name) {
		this.id = id;
		this.name = name;
	}
	@Id
	@Column(name = "id", unique = true, nullable = false)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name = "name", length = 32)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "owner", length = 54)
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	@Column(name = "leader_id", length = 32)
	public String getLeaderId() {
		return leaderId;
	}
	public void setLeaderId(String leaderId) {
		this.leaderId = leaderId;
	}
	@Column(name = "memo", length = 255)
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	@Column(name = "status", length = 255)
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Column(name = "province")
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	@Column(name = "city")
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	@Column(name = "address")
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Column(name = "create_time")
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	
}
