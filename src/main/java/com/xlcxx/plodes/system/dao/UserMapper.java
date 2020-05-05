package com.xlcxx.plodes.system.dao;


import com.xlcxx.config.MyMapper;
import com.xlcxx.plodes.system.domain.MyUser;
import com.xlcxx.plodes.system.domain.UserWithRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper extends MyMapper<MyUser> {

    int addUsersBatch(@Param("users") List<MyUser> users);

    List<MyUser> findUserWithDept(MyUser user);

    List<UserWithRole> findUserWithRole(Long userId);

    List<MyUser> findUserWithRoleId(String roleId);

    /**
     * 获取岗位下的人员
     **/
    List<MyUser> selectUser2StationDept(String station);

    int updateByQuitReason(@Param("ids") List<String> ids, @Param("quitReason") String quitReason);

    /**单纯查个人权限返回前端权限判断*/
    List<String> selectRoleByUserid(@Param("userid")String userid);

    List<MyUser> selectLocalUser();


}