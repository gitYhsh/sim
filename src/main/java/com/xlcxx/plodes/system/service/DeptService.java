package com.xlcxx.plodes.system.service;

import com.xlcxx.plodes.baseServices.IServices;
import com.xlcxx.plodes.system.domain.Dept;
import com.xlcxx.plodes.system.domain.MyUser;
import com.xlcxx.utils.ApiResult;

import java.util.List;

public interface DeptService extends IServices<Dept> {

    /**
     * 获取部门组织结构点击使用
     **/
    ApiResult getDeptOrgInfo2Tree(String deptid);

    /**
     * 获取组织架构树状
     **/
    ApiResult getDeptOrge(String deptid);

    /**
     * 获取部门通用的树状
     **/
    ApiResult getDeptTree(String deptid);

    /**
     * 修改部门基本信息
     **/
    ApiResult updateDeptInfo(Dept dept);

    /**
     * 获取部门的详情
     **/
    ApiResult getDeptInfo(String deptid);

    /**
     * 增加子部门
     **/
    ApiResult addSubDeptInfo(Dept dept);

    /**
     * 删除部门
     **/
    ApiResult deleteDept(Dept dept);

    Dept findDeptById(String deptid);

    /**
     * 查询子部门
     **/
    ApiResult getDeptByParentid(String deptid);

    /**
     * 查询一级显示部门
     **/
    List<Dept> getOneLevelShowDept();

    /**
     * 查询一级部门和 当前部门id
     **/
    ApiResult getSelectUsers(String parentid, String curentid);


}
