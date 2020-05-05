package com.xlcxx.plodes.busylogic.work.domian;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tg_work_week")
public class TgWorkWeek {

    @Id
    @Column(name = "ww_uuid")
    private String wwUuid;

    /**
     * 部门
     */
    @Column(name = "ww_dept_uuid")
    private String wwDeptUuid;

    /**部门名称**/
    @Transient
    private String  wwDeptName;

    /**
     * 工作计划的月份
     */
    @Column(name = "ww_work_time")
    private String wwWorkTime;

    /**
     * 责任人
     */
    @Column(name = "ww_dutyer")
    private String wwDutyer;

    /**责任人的名称**/
    @Transient
    private String wwDutyName;
    /**
     * 完成时间
     */
    @Column(name = "ww_endtime")
    private String wwEndtime;

    /**
     * 发布状态1 未提交 2已提交未审核  3已审核 4:拒审
     */
    @Column(name = "ww_rl_status")
    private String wwRlStatus;

    /**
     * 主管点击监督任务的状态 0:默认 1:完成  2: 未完成 3:延时 4:已考核 5:不考核 6:结束完
     */
    @Column(name = "ww_status")
    private String wwStatus;

    /**
     * 1未审核 2已审核3 申诉中 4已完结5 确认6默认
     */
    @Column(name = "ww_iskao")
    private String wwIskao;

    /**
     * 创建人
     */
    @Column(name = "ww_creator")
    private String wwCreator;

    /**
     * 创建时间
     */
    @Column(name = "ww_creatime")
    private String wwCreatime;

    @Column(name = "ww_version")
    private int wwVersion;

    /**
     * 上传模板内容
     */
    @Column(name = "ww_json")
    private String wwJson;

    @Column(name = "ww_content")
    private String wwContent;

    /**
     * 完成标准
     */
    @Column(name = "ww_startant")
    private String wwStartant;

    /**
     * 批注
     */
    @Column(name = "ww_picontent")
    private String wwPicontent;


    public TgWorkWeek() {
    }

    public TgWorkWeek(String wwUuid, String wwRlStatus,  String wwCreatime, int wwVersion) {
        this.wwUuid = wwUuid;
        this.wwRlStatus = wwRlStatus;
        this.wwCreatime = wwCreatime;
        this.wwVersion = wwVersion;
    }

    public String getWwDeptName() {
        return wwDeptName;
    }

    public void setWwDeptName(String wwDeptName) {
        this.wwDeptName = wwDeptName;
    }

    public String getWwDutyName() {
        return wwDutyName;
    }

    public void setWwDutyName(String wwDutyName) {
        this.wwDutyName = wwDutyName;
    }

    /**
     * @return ww_uuid
     */
    public String getWwUuid() {
        return wwUuid;
    }

    /**
     * @param wwUuid
     */
    public void setWwUuid(String wwUuid) {
        this.wwUuid = wwUuid == null ? null : wwUuid.trim();
    }

    /**
     * 获取部门
     *
     * @return ww_dept_uuid - 部门
     */
    public String getWwDeptUuid() {
        return wwDeptUuid;
    }

    /**
     * 设置部门
     *
     * @param wwDeptUuid 部门
     */
    public void setWwDeptUuid(String wwDeptUuid) {
        this.wwDeptUuid = wwDeptUuid == null ? null : wwDeptUuid.trim();
    }

    /**
     * 获取工作计划的月份
     *
     * @return ww_work_time - 工作计划的月份
     */
    public String getWwWorkTime() {
        return wwWorkTime;
    }

    /**
     * 设置工作计划的月份
     *
     * @param wwWorkTime 工作计划的月份
     */
    public void setWwWorkTime(String wwWorkTime) {
        this.wwWorkTime = wwWorkTime == null ? null : wwWorkTime.trim();
    }

    /**
     * 获取责任人
     *
     * @return ww_dutyer - 责任人
     */
    public String getWwDutyer() {
        return wwDutyer;
    }

    /**
     * 设置责任人
     *
     * @param wwDutyer 责任人
     */
    public void setWwDutyer(String wwDutyer) {
        this.wwDutyer = wwDutyer == null ? null : wwDutyer.trim();
    }

    /**
     * 获取完成时间
     *
     * @return ww_endtime - 完成时间
     */
    public String getWwEndtime() {
        return wwEndtime;
    }

    /**
     * 设置完成时间
     *
     * @param wwEndtime 完成时间
     */
    public void setWwEndtime(String wwEndtime) {
        this.wwEndtime = wwEndtime;
    }

    /**
     * 获取发布状态1 未提交 2已提交未审核  3已审核 4:拒审
     *
     * @return ww_rl_status - 发布状态1 未提交 2已提交未审核  3已审核 4:拒审
     */
    public String getWwRlStatus() {
        return wwRlStatus;
    }

    /**
     * 设置发布状态1 未提交 2已提交未审核  3已审核 4:拒审
     *
     * @param wwRlStatus 发布状态1 未提交 2已提交未审核  3已审核 4:拒审
     */
    public void setWwRlStatus(String wwRlStatus) {
        this.wwRlStatus = wwRlStatus == null ? null : wwRlStatus.trim();
    }

    /**
     * 获取主管点击监督任务的状态 0:默认 1:完成  2: 未完成 3:延时 4:已考核 5:不考核 6:结束完
     *
     * @return ww_status - 主管点击监督任务的状态 0:默认 1:完成  2: 未完成 3:延时 4:已考核 5:不考核 6:结束完
     */
    public String getWwStatus() {
        return wwStatus;
    }

    /**
     * 设置主管点击监督任务的状态 0:默认 1:完成  2: 未完成 3:延时 4:已考核 5:不考核 6:结束完
     *
     * @param wwStatus 主管点击监督任务的状态 0:默认 1:完成  2: 未完成 3:延时 4:已考核 5:不考核 6:结束完
     */
    public void setWwStatus(String wwStatus) {
        this.wwStatus = wwStatus == null ? null : wwStatus.trim();
    }

    /**
     * 获取1未审核 2已审核3 申诉中 4已完结5 确认6默认
     *
     * @return ww_iskao - 1未审核 2已审核3 申诉中 4已完结5 确认6默认
     */
    public String getWwIskao() {
        return wwIskao;
    }

    /**
     * 设置1未审核 2已审核3 申诉中 4已完结5 确认6默认
     *
     * @param wwIskao 1未审核 2已审核3 申诉中 4已完结5 确认6默认
     */
    public void setWwIskao(String wwIskao) {
        this.wwIskao = wwIskao == null ? null : wwIskao.trim();
    }

    /**
     * 获取创建人
     *
     * @return ww_creator - 创建人
     */
    public String getWwCreator() {
        return wwCreator;
    }

    /**
     * 设置创建人
     *
     * @param wwCreator 创建人
     */
    public void setWwCreator(String wwCreator) {
        this.wwCreator = wwCreator == null ? null : wwCreator.trim();
    }

    /**
     * 获取创建时间
     *
     * @return ww_creatime - 创建时间
     */
    public String getWwCreatime() {
        return wwCreatime;
    }

    /**
     * 设置创建时间
     *
     * @param wwCreatime 创建时间
     */
    public void setWwCreatime(String wwCreatime) {
        this.wwCreatime = wwCreatime;
    }

    /**
     * @return ww_version
     */
    public int getWwVersion() {
        return wwVersion;
    }

    /**
     * @param wwVersion
     */
    public void setWwVersion(int wwVersion) {
        this.wwVersion = wwVersion;
    }

    /**
     * 获取上传模板内容
     *
     * @return ww_json - 上传模板内容
     */
    public String getWwJson() {
        return wwJson;
    }

    /**
     * 设置上传模板内容
     *
     * @param wwJson 上传模板内容
     */
    public void setWwJson(String wwJson) {
        this.wwJson = wwJson == null ? null : wwJson.trim();
    }

    /**
     * @return ww_content
     */
    public String getWwContent() {
        return wwContent;
    }

    /**
     * @param wwContent
     */
    public void setWwContent(String wwContent) {
        this.wwContent = wwContent == null ? null : wwContent.trim();
    }

    /**
     * 获取完成标准
     *
     * @return ww_startant - 完成标准
     */
    public String getWwStartant() {
        return wwStartant;
    }

    /**
     * 设置完成标准
     *
     * @param wwStartant 完成标准
     */
    public void setWwStartant(String wwStartant) {
        this.wwStartant = wwStartant == null ? null : wwStartant.trim();
    }

    /**
     * 获取批注
     *
     * @return ww_picontent - 批注
     */
    public String getWwPicontent() {
        return wwPicontent;
    }

    /**
     * 设置批注
     *
     * @param wwPicontent 批注
     */
    public void setWwPicontent(String wwPicontent) {
        this.wwPicontent = wwPicontent == null ? null : wwPicontent.trim();
    }

    @Override
    public String toString() {
        return "TgWorkWeek{" +
                "wwUuid='" + wwUuid + '\'' +
                ", wwDeptUuid='" + wwDeptUuid + '\'' +
                ", wwWorkTime='" + wwWorkTime + '\'' +
                ", wwDutyer='" + wwDutyer + '\'' +
                ", wwEndtime='" + wwEndtime + '\'' +
                ", wwRlStatus='" + wwRlStatus + '\'' +
                ", wwStatus='" + wwStatus + '\'' +
                ", wwIskao='" + wwIskao + '\'' +
                ", wwCreator='" + wwCreator + '\'' +
                ", wwCreatime='" + wwCreatime + '\'' +
                ", wwVersion=" + wwVersion +
                ", wwJson='" + wwJson + '\'' +
                ", wwContent='" + wwContent + '\'' +
                ", wwStartant='" + wwStartant + '\'' +
                ", wwPicontent='" + wwPicontent + '\'' +
                '}';
    }
}