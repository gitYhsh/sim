package com.xlcxx.plodes.system.dao;

import com.xlcxx.config.MyMapper;
import com.xlcxx.plodes.system.domain.UserWithDept;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserWithDeptMapper extends MyMapper<UserWithDept> {
    /**
     * 查询自定义角色管理得部门
     **/
    List<UserWithDept> getUserWithDept(String roleId, String userid);
}