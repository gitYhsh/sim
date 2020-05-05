package com.xlcxx.plodes.system.dao;

import com.xlcxx.config.MyMapper;
import com.xlcxx.plodes.system.domain.MyUser;
import com.xlcxx.plodes.system.domain.Role;
import com.xlcxx.plodes.system.domain.RoleWithMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleMapper extends MyMapper<Role> {

    List<Role> findUserRole(String userName);

    List<RoleWithMenu> findById(Long roleId);

    /**
     * 获取角色下的人员
     **/
    List<MyUser> getDefaultRoleUser(String roleId);

    /**
     * 查询人的角色名称
     **/
    List<Role> selectUserRoleByUsername(String username);



}