package com.xlcxx.plodes.system.domain;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "t_role")
public class Role implements Serializable {

	private static final long serialVersionUID = -1714476694755654924L;

	@Id
	@GeneratedValue(generator = "JDBC")
	@Column(name = "ROLE_ID")
	private Long roleId;

	@Column(name = "ROLE_NAME")
	private String roleName;

	@Column(name = "ROLE_TYPE")
	private String roleType;

	/**
	 * @return ROLE_ID
	 */
	public Long getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return ROLE_NAME
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * @param roleName
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName == null ? null : roleName.trim();
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
}