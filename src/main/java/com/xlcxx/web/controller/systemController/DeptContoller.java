package com.xlcxx.web.controller.systemController;

import com.xlcxx.plodes.system.domain.Dept;
import com.xlcxx.plodes.system.service.DeptService;
import com.xlcxx.utils.ApiResult;
import com.xlcxx.web.controller.baseController.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;


@RestController
@Api(tags = "组织结构API")
public class DeptContoller extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(DeptContoller.class);

    @Autowired
    private DeptService deptService;

    @ApiOperation(value = "查询所有得自定义部门得人员信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deptid", value = "部门id 一级组织id 1", dataType = "String", paramType = "query"),
    })

    @GetMapping(value = "dept/selectDeptToByDeptid")
    public ApiResult selectCusDeptToUserByDeptid(String deptid) {
        return deptService.getDeptOrgInfo2Tree(deptid);
    }

    @ApiOperation(value = "查询组织结构")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deptid", value = "部门id 一级组织id 1", dataType = "String", paramType = "query"),
    })
    @GetMapping(value = "dept/getDeptOrge")
    public ApiResult getDeptOrge(String deptid) {
        return deptService.getDeptOrge(deptid);
    }

    @ApiOperation(value = "查询组织结构树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deptid", value = "部门id 一级组织id 1", dataType = "String", paramType = "query"),
    })
    @GetMapping(value = "dept/getDeptTree")
    public ApiResult getDeptTree(String deptid) {
        return deptService.getDeptTree(deptid);
    }


    @ApiOperation(value = "修改部门的基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deptId", value = "部门id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "parentId", value = "上级部门id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "deptName", value = "部门名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "deptDirector", value = "部门主管usename", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "deptType", value = "是否是显示部门(0否，1是)", dataType = "String", paramType = "query"),
    })
    @GetMapping(value = "dept/updateDeptInfo")
    public ApiResult updateDeptInfo(@ApiIgnore Dept dept) {
        return deptService.updateDeptInfo(dept);
    }

    @ApiOperation(value = "获取部门详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deptId", value = "部门id", required = true, dataType = "String", paramType = "query"),
    })
    @GetMapping(value = "dept/getDeptInfo")
    public ApiResult getDeptInfo(String deptId) {
        return deptService.getDeptInfo(deptId);
    }


    @ApiOperation(value = "增加子部门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId", value = "上级部门id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "deptName", value = "部门名称", required = true, dataType = "String", paramType = "query"),
    })
    @GetMapping(value = "dept/addSubDeptInfo")
    public ApiResult addSubDeptInfo(@ApiIgnore Dept dept) {
        return deptService.addSubDeptInfo(dept);
    }

    @ApiOperation(value = "删除部门(有部门和人员不能删)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deptId", value = "部门id", required = true, dataType = "String", paramType = "query"),
    })

    @GetMapping(value = "dept/deleteDeptInfo")
    public ApiResult deleteDeptInfo(@ApiIgnore Dept dept) {
        return deptService.deleteDept(dept);
    }

    @ApiOperation(value = "查询部门的子部门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deptId", value = "部门id", required = true, dataType = "String", paramType = "query"),
    })
    @GetMapping(value = "dept/getDeptByParentid")
    public ApiResult getDeptByParentid(String deptId) {
        return deptService.getDeptByParentid(deptId);
    }

    @ApiOperation(value = "查询一级显示的部门")
    @GetMapping(value = "dept/getOneLevelShowDept")
    public ApiResult getOneLevelShowDept() {
        try {
            List<Dept> list = deptService.getOneLevelShowDept();
            return ApiResult.ok(list);
        } catch (Exception me) {

        }
        return ApiResult.error("查询一级部门失败");
    }

    @ApiOperation(value = "查询选择组织结构(部门和人)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentid", value = "上级部门id", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "curentid", value = "当前部门id", dataType = "String", paramType = "query"),
    })
    @GetMapping(value = "dept/getDeptAndUsers")
    public ApiResult getDeptAndUsers(String parentid, String curentid) {
        if (StringUtils.isAllEmpty(parentid, curentid)) {
            return ApiResult.error("传入的数据为空");
        }
        return deptService.getSelectUsers(parentid, curentid);
    }


}
