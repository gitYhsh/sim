package com.xlcxx.plodes.system.domain;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "t_user_dept")
public class UserWithDept {

    @Column(name = "role_id")
    private String roleId;

    @Column(name = "user_id")
    private String userId;

    private String deptid;


    @Transient
    private String deptName;


    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    /**
     * @return user_id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * @return deptid
     */
    public String getDeptid() {
        return deptid;
    }

    /**
     * @param deptid
     */
    public void setDeptid(String deptid) {
        this.deptid = deptid == null ? null : deptid.trim();
    }
}