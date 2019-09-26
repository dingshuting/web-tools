package com.ijs.core.base.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.google.gson.annotations.Expose;
import com.ijs.core.base.Config;
import com.ijs.core.base.unit.UserFuncContainer;
import com.ijs.core.component.anno.DefaultConstructorForList;

/**
 * Userinfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sys_user")
public class User extends AbstractUser implements UserDetails,
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4687514318668199116L;
	@Expose
	private Set<Role> roles = new HashSet<Role>(0);
	
	private UserFuncContainer funcs;

	private Integer isAdmin;
	private boolean isSa;
	private SysDep sysDep;
	private String newPassword;
	//存储身份信息的对象，如专家、企业等扩展信息
	@Expose
	private Object identity;
	private String roleStr;
	//保存用户的运行配置信息，例如是否及时通信工具在线，是否接收消息等开关。
    private Map<String,String> setting=new HashMap<>();
	/**
	 * 用户状态
	 * @author
	 */
	public interface UserStatus{
		/**
		 * 无效
		 */
		public static Integer INVALID=0;
		/**
		 * 未确认
		 */
		public static Integer UNCONFIRMED=1;
		
		/**
		 * 正常
		 */
		public static Integer NORMAL=2;
	}
	/** default constructor */
	public User() {
		funcs = new UserFuncContainer();
	}
	
	public User(String id){
		this.setId(id);
	}

	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
	      name="sys_user_role",
	      joinColumns={@JoinColumn(name="user_id", referencedColumnName="ID")},
	      inverseJoinColumns={@JoinColumn(name="role_id", referencedColumnName="ID")})
	public Set<Role> getRoles() {
		if(roles == null){
			roles = new HashSet<Role>();
		}

		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	
	@Transient
	public Collection<GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new LinkedHashSet<GrantedAuthority>();
		authorities.addAll(this.getRoles());
		authorities.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				// TODO Auto-generated method stub
				return "ROLE_USER";
			}
		});
		return authorities;
	}

	@Transient
	public boolean isAccountNonExpired() {
		return true;
	}

	@Transient
	public boolean isAccountNonLocked() {
		return true;
	}

	@Transient
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Transient
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof User)) {
			return false;
		}

		final User user = (User) o;

		return this.getId() != null ? this.getId() == user.getId() : false;
	}

	@Transient
	public int hashCode() {
		return (this.getAccountNo() != null ? this.getAccountNo().hashCode()
				: 0);
	}


	@Transient
	public String getUsername() {
		return this.getAccountNo();
	}

	@Transient
	public boolean isEnabled() {
		if (this.getStatus() != null && this.getStatus() == 2) {
			return true;
		} else {
			return false;
		}
	}

	@Transient
	public UserFuncContainer getFuncs() {
		return funcs;
	}

	@Transient
	public String getKey() {
		return (this.getId() != null ? this.getId().toString() : "none");
	}
	@Transient
	public Integer getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Integer isAdmin) {
		this.isAdmin = isAdmin;
	}
	@Transient
	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return Config.gson.toJson(this);
	}
	@Transient
	public SysDep getSysDep() {
		return sysDep;
	}

	public void setSysDep(SysDep sysDep) {
		this.sysDep = sysDep;
	}
	@Transient
	public String getRoleStr() {
		return roleStr;
	}

	public void setRoleStr(String roleStr) {
		this.roleStr = roleStr;
	}
	@Transient
	public Map<String, String> getSetting() {
		return setting;
	}

	public void setSetting(Map<String, String> setting) {
		this.setting = setting;
	}
	@Transient
	public Object getIdentity() {
		return identity;
	}

	public void setIdentity(Object identity) {
		this.identity = identity;
	}

	public void setFuncs(UserFuncContainer funcs) {
		this.funcs = funcs;
	}

	public boolean hasRole(String roleStr) {
		for(Role role:this.roles) {
			if(role.getId().equals(roleStr)||(role.getCode()!=null&&role.getCode().equals(roleStr))) {
				return true;
			}
		}
		return false;
	}
	@Transient
	public boolean isSa() {
		return isSa;
	}

	public void setSa(boolean isSa) {
		this.isSa = isSa;
	}
	

	

}
