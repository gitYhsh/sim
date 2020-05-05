package com.xlcxx.plodes.system.domain;

import javax.persistence.*;
import java.util.List;

@Table(name = "t_notype")
public class NoType {
    @Id
    @Column(name = "nt_id")
    @GeneratedValue(generator = "JDBC")
    private Integer ntId;

    /**
     * 通知类型
     */
    @Column(name = "nt_name")
    private String ntName;

    /**
     * 父级名称
     */
    @Column(name = "nt_pid")
    private Integer ntPid;

    @Column(name = "nt_creatime")
    private String ntCreatime;

    /**
     * 状态删除 0： 正常 1：删除
     */
    @Column(name = "nt_status")
    private String ntStatus;

    @Transient
    private List<NoType> chiles;

    public List<NoType> getChiles() {
        return chiles;
    }

    public void setChiles(List<NoType> chiles) {
        this.chiles = chiles;
    }

    /**
     * @return nt_id
     */
    public Integer getNtId() {
        return ntId;
    }

    /**
     * @param ntId
     */
    public void setNtId(Integer ntId) {
        this.ntId = ntId;
    }

    /**
     * 获取通知类型
     *
     * @return nt_name - 通知类型
     */
    public String getNtName() {
        return ntName;
    }

    /**
     * 设置通知类型
     *
     * @param ntName 通知类型
     */
    public void setNtName(String ntName) {
        this.ntName = ntName == null ? null : ntName.trim();
    }

    /**
     * 获取父级名称
     *
     * @return nt_pid - 父级名称
     */
    public Integer getNtPid() {
        return ntPid;
    }

    /**
     * 设置父级名称
     *
     * @param ntPid 父级名称
     */
    public void setNtPid(Integer ntPid) {
        this.ntPid = ntPid;
    }

    /**
     * @return nt_creatime
     */
    public String getNtCreatime() {
        return ntCreatime;
    }

    /**
     * @param ntCreatime
     */
    public void setNtCreatime(String ntCreatime) {
        this.ntCreatime = ntCreatime;
    }

    /**
     * 获取状态删除 0： 正常 1：删除
     *
     * @return nt_status - 状态删除 0： 正常 1：删除
     */
    public String getNtStatus() {
        return ntStatus;
    }

    /**
     * 设置状态删除 0： 正常 1：删除
     *
     * @param ntStatus 状态删除 0： 正常 1：删除
     */
    public void setNtStatus(String ntStatus) {
        this.ntStatus = ntStatus == null ? null : ntStatus.trim();
    }
}