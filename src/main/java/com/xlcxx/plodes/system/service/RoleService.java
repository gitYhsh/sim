package com.xlcxx.plodes.system.service;


import com.xlcxx.plodes.baseServices.IServices;
import com.xlcxx.plodes.system.domain.MyUser;
import com.xlcxx.plodes.system.domain.Role;
import com.xlcxx.plodes.system.domain.RoleWithMenu;
import com.xlcxx.plodes.system.domain.UserWithDept;
import com.xlcxx.utils.ApiResult;

import java.util.List;

public interface RoleService extends IServices<Role> {

    RoleWithMenu findRoleWithMenus(Long roleId);

    /**
     * 分页获取角色下的人员
     **/
    List<MyUser> getPageUserWithRole(String roleId);

    /**
     * 修改保存
     **/
    ApiResult editUpdateRole(Role role);

    void addRole(Role role, Long[] menuIds);

    void updateRole(Role role, String menuIds);

    void deleteRoles(String roleIds) throws Exception;

    /**
     * 增加角色权限
     **/
    ApiResult setUpdateRole(String roleIds, String menuid, String type);

    /**
     * 新增自定义角色
     ***/
    ApiResult addCusRole(Role role);

    /**
     * 获取所有的自定义角色的信息
     **/
    ApiResult getCustonRole();

    /**
     * 设置自定义角色的管理部门
     **/
    ApiResult addCusRoleUser2Dept(UserWithDept userWithDept);

    /**
     * 获取默认角色下的人员
     **/
    ApiResult getDefaultUser(String roleid);

}
