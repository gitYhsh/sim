package com.xlcxx.plodes.system.service;


import com.xlcxx.plodes.baseServices.IServices;
import com.xlcxx.plodes.system.domain.Dept;
import com.xlcxx.plodes.system.domain.MyUser;
import com.xlcxx.utils.ApiResult;
import com.xlcxx.utils.QueryRequest;

import java.util.List;

public interface UserService extends IServices<MyUser> {
    /**
     * 获取所有的在职的员工
     **/
    List<MyUser> getAllMyUser();

    /**
     * 增加人员
     **/
    ApiResult addUserByDept(MyUser user) throws Exception;

    /**
     * 逻辑删除
     **/
    ApiResult batchUpdateUser(String userIds, String quitReason) throws Exception;

    /**
     * 根据部门id查人员
     */
    List<MyUser> selectUserByDeptid(String deptid);

    /**
     * 根据角色查找人员
     **/
    List<MyUser> findUserWithRoleId(String roleId);

    /**
     * 获取部门的人员
     **/
    List<MyUser> findUserWithDepts(List<String> deptid);

    /**
     * 人员调整部门
     **/
    ApiResult adjustUser2Dept(String username, String deptid) throws Exception;

    /**
     * 根据username查user对象
     **/
    MyUser findUserByUsername(String username);

    /**
     * 处理多责任人姓名
     */
    String checkNickNames(String usernames);

    /**
     * 人员更改岗位
     **/
    ApiResult adjustUser2Station(String userid, String station);

    /**
     * 查询岗位人员
     **/
    ApiResult getStationUser(String station);

    /**
     * 获取个人管理部门
     **/
    List<Dept> getPersonManageDepts(String userid);

    /**
     * 获取个人管理部门
     **/
    ApiResult getPersonDept(String username);

    /**
     * 查询个人详情
     **/
    MyUser getUserDetail(String username);

    /**
     * 根据部门查人员列表
     */
    List<MyUser> getUserListByDeptid(MyUser user, QueryRequest request);


    /**
     * 获取用户列表
     **/
    //@Cacheable(value="users", key="#p0")
    List<MyUser> getAllUser(String username);



}
