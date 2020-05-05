package com.xlcxx.plodes.system.service;


import com.xlcxx.plodes.baseServices.IServices;
import com.xlcxx.plodes.system.domain.UserRole;
import com.xlcxx.utils.ApiResult;

import java.util.List;


public interface UserRoleService extends IServices<UserRole> {

    void deleteUserRolesByRoleId(String roleIds);

    void deleteUserRolesByUserId(String userIds);

    /**
     * 删除自定角色时 同时删除相应角色下配置得人员管理部门
     **/
    void deleteUserRoleDeptByRoleId(String roleIds);

    /**
     * 自定义用户增加人员
     **/
    ApiResult addUserRoles(String userRole, String userid);

    /**
     * 删除自定义角色相应得员工
     **/
    ApiResult deleteCusRoleUserAndDept(String roleId, String usersid) throws Exception;

    /**
     * 获取默认角色的信息
     **/
    List<UserRole> getAllcharge(String roleid);

    /**
     * 单纯查个人权限返回前端权限判断
     */
    String selectPowerDataByUserid(String userid);


}
