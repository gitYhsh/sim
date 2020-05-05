package com.xlcxx.plodes.system.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "t_dept")
public class Dept {
    /**
     * 部门ID
     */
    @Id
    @Column(name = "dept_id")
    private Long deptId;

    /**
     * 上级部门ID
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 部门名称
     */
    @Column(name = "dept_name")
    private String deptName;

    /**
     * 是不是基础部门 0 否 1是 基础部门不能删除
     */
    @Column(name = "dept_status")
    private String deptStatus;

    /**
     * 是不是作为一级部门显示 0否，1是
     */
    @Column(name = "dept_type")
    private String deptType;

    /**
     * 部门是否删除了 0 否 1是
     */
    @Column(name = "dept_del")
    private String deptDel;

    /**
     * 部门主管id
     */
    @Column(name = "dept_director")
    private String deptDirector;

    @Column(name = "dept_creattime")
    private String deptCreattime;

    /**成绩*/
    @Column(name = "dept_score")
    private String deptScore;



    /**
     * 获取部门ID
     *
     * @return dept_id - 部门ID
     */
    public Long getDeptId() {
        return deptId;
    }

    /**
     * 设置部门ID
     *
     * @param deptId 部门ID
     */
    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    /**
     * 获取上级部门ID
     *
     * @return parent_id - 上级部门ID
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置上级部门ID
     *
     * @param parentId 上级部门ID
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取部门名称
     *
     * @return dept_name - 部门名称
     */
    public String getDeptName() {
        return deptName;
    }

    /**
     * 设置部门名称
     *
     * @param deptName 部门名称
     */
    public void setDeptName(String deptName) {
        this.deptName = deptName == null ? null : deptName.trim();
    }

    /**
     * 获取是不是基础部门 0 否 1是 基础部门不能删除
     *
     * @return dept_status - 是不是基础部门 0 否 1是 基础部门不能删除
     */
    public String getDeptStatus() {
        return deptStatus;
    }

    /**
     * 设置是不是基础部门 0 否 1是 基础部门不能删除
     *
     * @param deptStatus 是不是基础部门 0 否 1是 基础部门不能删除
     */
    public void setDeptStatus(String deptStatus) {
        this.deptStatus = deptStatus == null ? null : deptStatus.trim();
    }

    /**
     * 获取是不是作为一级部门显示 0否，1是
     *
     * @return dept_type - 是不是作为一级部门显示 0否，1是
     */
    public String getDeptType() {
        return deptType;
    }

    /**
     * 设置是不是作为一级部门显示 0否，1是
     *
     * @param deptType 是不是作为一级部门显示 0否，1是
     */
    public void setDeptType(String deptType) {
        this.deptType = deptType == null ? null : deptType.trim();
    }

    /**
     * 获取部门是否删除了 0 否 1是
     *
     * @return dept_del - 部门是否删除了 0 否 1是
     */
    public String getDeptDel() {
        return deptDel;
    }

    /**
     * 设置部门是否删除了 0 否 1是
     *
     * @param deptDel 部门是否删除了 0 否 1是
     */
    public void setDeptDel(String deptDel) {
        this.deptDel = deptDel == null ? null : deptDel.trim();
    }

    /**
     * 获取部门主管id
     *
     * @return dept_director - 部门主管id
     */
    public String getDeptDirector() {
        return deptDirector;
    }

    /**
     * 设置部门主管id
     *
     * @param deptDirector 部门主管id
     */
    public void setDeptDirector(String deptDirector) {
        this.deptDirector = deptDirector == null ? null : deptDirector.trim();
    }

    /**
     * @return dept_creattime
     */
    public String getDeptCreattime() {
        return deptCreattime;
    }

    /**
     * @param deptCreattime
     */
    public void setDeptCreattime(String deptCreattime) {
        this.deptCreattime = deptCreattime;
    }

    public String getDeptScore() {
        return deptScore;
    }

    public void setDeptScore(String deptScore) {
        this.deptScore = deptScore;
    }
}