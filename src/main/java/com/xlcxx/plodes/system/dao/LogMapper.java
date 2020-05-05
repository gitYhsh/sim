package com.xlcxx.plodes.system.dao;

import com.xlcxx.config.MyMapper;
import com.xlcxx.plodes.system.domain.SysLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogMapper extends MyMapper<SysLog> {
}