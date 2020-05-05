package com.xlcxx.plodes.system.dao;


import com.xlcxx.config.MyMapper;
import com.xlcxx.plodes.system.domain.UserRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRoleMapper extends MyMapper<UserRole> {
}