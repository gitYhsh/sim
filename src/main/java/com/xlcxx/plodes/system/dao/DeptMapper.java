package com.xlcxx.plodes.system.dao;

import com.xlcxx.config.MyMapper;
import com.xlcxx.plodes.system.domain.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description: taskmanage
 * Created by yhsh on 2019/11/5 16:20
 * version 2.0
 * 方法说明
 */
@Mapper
public interface DeptMapper extends MyMapper<Dept> {

    /**获取岗位的部门**/
    List<Dept> seelctDeptByTypeId();

}