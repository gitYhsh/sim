package com.xlcxx.plodes.system.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "t_basic_set")
public class TBasicSet {
    @Id
    @Column(name = "tb_id")
    private String tbId;

    /**
     * 父级节点
     */
    @Column(name = "tb_pid")
    private String tbPid;

    /**
     * 名称
     */
    @Column(name = "tb_name")
    private String tbName;

    /**
     * 类型 0：通知 1：公司通式制度 2：部门交接规范
     */
    @Column(name = "tb_type")
    private String tbType;

    /**
     * 1 删除
     */
    @Column(name = "tb_status")
    private String tbStatus;

    /**
     * 事件
     */
    @Column(name = "tb_time")
    private Date tbTime;

    /**
     * @return tb_id
     */
    public String getTbId() {
        return tbId;
    }

    /**
     * @param tbId
     */
    public void setTbId(String tbId) {
        this.tbId = tbId;
    }

    /**
     * 获取父级节点
     *
     * @return tb_pid - 父级节点
     */
    public String getTbPid() {
        return tbPid;
    }

    /**
     * 设置父级节点
     *
     * @param tbPid 父级节点
     */
    public void setTbPid(String tbPid) {
        this.tbPid = tbPid;
    }

    /**
     * 获取名称
     *
     * @return tb_name - 名称
     */
    public String getTbName() {
        return tbName;
    }

    /**
     * 设置名称
     *
     * @param tbName 名称
     */
    public void setTbName(String tbName) {
        this.tbName = tbName == null ? null : tbName.trim();
    }

    /**
     * 获取类型 0：通知 1：公司通式制度 2：部门交接规范
     *
     * @return tb_type - 类型 0：通知 1：公司通式制度 2：部门交接规范
     */
    public String getTbType() {
        return tbType;
    }

    /**
     * 设置类型 0：通知 1：公司通式制度 2：部门交接规范
     *
     * @param tbType 类型 0：通知 1：公司通式制度 2：部门交接规范
     */
    public void setTbType(String tbType) {
        this.tbType = tbType == null ? null : tbType.trim();
    }

    /**
     * 获取1 删除
     *
     * @return tb_status - 1 删除
     */
    public String getTbStatus() {
        return tbStatus;
    }

    /**
     * 设置1 删除
     *
     * @param tbStatus 1 删除
     */
    public void setTbStatus(String tbStatus) {
        this.tbStatus = tbStatus == null ? null : tbStatus.trim();
    }

    /**
     * 获取事件
     *
     * @return tb_time - 事件
     */
    public Date getTbTime() {
        return tbTime;
    }

    /**
     * 设置事件
     *
     * @param tbTime 事件
     */
    public void setTbTime(Date tbTime) {
        this.tbTime = tbTime;
    }
}