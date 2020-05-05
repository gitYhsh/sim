package com.xlcxx.plodes.system.domain;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@Table(name = "t_user")
public class MyUser implements Serializable {

    private static final long serialVersionUID = -4852732617765810959L;

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "username")
    private String username;

    /**昵称**/
    @Column(name = "nickname" )
    private  String nickname;

    @Column(name = "dept_id")
    private Long deptId;

    @Column(name = "status")
    private String status ;

    @Column(name = "station")
    private String station;

    @Column(name = "quit_reason")
    private String quitReason;//离职原因


    @Transient
    private List<String> allAuthority;// 权限码


    @Transient
    private String deptName ;

    public String getQuitReason() {
        return quitReason;
    }

    public void setQuitReason(String quitReason) {
        this.quitReason = quitReason;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public List<String> getAllAuthority() {
        return allAuthority;
    }

    public void setAllAuthority(List<String> allAuthority) {
        this.allAuthority = allAuthority;
    }

    /**
     * @return USER_ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return USERNAME
     */
    public String getUsername() {
        return username;
    }



    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }



    /**
     * @return DEPT_ID
     */
    public Long getDeptId() {
        return deptId;
    }

    /**
     * @param deptId
     */
    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "MyUser{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", deptId=" + deptId +
                ", status='" + status + '\'' +
                ", allAuthority=" + allAuthority +
                ", deptName='" + deptName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof MyUser && StringUtils.equals(this.username, ((MyUser) o).username);
    }

    @Override
    public int hashCode() {

        return this.username.hashCode();
    }


}