package com.xlcxx.plodes.system.service;


import com.xlcxx.plodes.baseServices.IServices;
import com.xlcxx.plodes.system.domain.RoleMenu;

import java.util.List;

public interface RoleMenuServie extends IServices<RoleMenu> {

    void deleteRoleMenusByRoleId(String roleIds);

    void deleteRoleMenusByMenuId(String menuIds);

    /**
     * 获取用户的角色信息
     **/
    List<RoleMenu> getUserRoleByUsername(String username);
}
