package com.xlcxx.plodes.system.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "t_station")
public class Station {
    /**
     * 岗位id
     */
    @Id
    @Column(name = "ts_id")
    @GeneratedValue(generator = "JDBC")
    private Integer tsId;

    /**
     * 岗位名称
     */
    @Column(name = "ts_name")
    private String tsName;

    /**
     * 父级岗位组
     */
    @Column(name = "ts_pid")
    private Integer tsPid;

    /**
     * 状态 0 删除 1 正常
     */
    @Column(name = "ts_status")
    private String tsStatus;

    /**
     * 创建时间
     */
    @Column(name = "ts_time")
    private String tsTime;

    /**
     * 获取岗位id
     *
     * @return ts_id - 岗位id
     */
    public Integer getTsId() {
        return tsId;
    }

    /**
     * 设置岗位id
     *
     * @param tsId 岗位id
     */
    public void setTsId(Integer tsId) {
        this.tsId = tsId;
    }

    /**
     * 获取岗位名称
     *
     * @return ts_name - 岗位名称
     */
    public String getTsName() {
        return tsName;
    }

    /**
     * 设置岗位名称
     *
     * @param tsName 岗位名称
     */
    public void setTsName(String tsName) {
        this.tsName = tsName == null ? null : tsName.trim();
    }

    /**
     * 获取父级岗位组
     *
     * @return ts_pid - 父级岗位组
     */
    public Integer getTsPid() {
        return tsPid;
    }

    /**
     * 设置父级岗位组
     *
     * @param tsPid 父级岗位组
     */
    public void setTsPid(Integer tsPid) {
        this.tsPid = tsPid;
    }

    /**
     * 获取状态 0 删除 1 正常
     *
     * @return ts_status - 状态 0 删除 1 正常
     */
    public String getTsStatus() {
        return tsStatus;
    }

    /**
     * 设置状态 0 删除 1 正常
     *
     * @param tsStatus 状态 0 删除 1 正常
     */
    public void setTsStatus(String tsStatus) {
        this.tsStatus = tsStatus == null ? null : tsStatus.trim();
    }

    /**
     * 获取创建时间
     *
     * @return ts_time - 创建时间
     */
    public String getTsTime() {
        return tsTime;
    }

    /**
     * 设置创建时间
     *
     * @param tsTime 创建时间
     */
    public void setTsTime(String tsTime) {
        this.tsTime = tsTime;
    }
}