package com.xlcxx.web.controller.systemController;


import com.xlcxx.plodes.system.domain.Role;
import com.xlcxx.plodes.system.domain.UserWithDept;
import com.xlcxx.plodes.system.service.RoleService;
import com.xlcxx.plodes.system.service.UserRoleService;
import com.xlcxx.utils.ApiResult;
import com.xlcxx.utils.QueryRequest;
import com.xlcxx.web.controller.baseController.BaseController;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;


@RestController
@Api(tags = "角色相关api")
public class RoleController extends BaseController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;

    @ApiOperation(value = "获取角色信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "String", paramType = "query"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "调用成功", response = ApiResult.class),
            @ApiResponse(code = 500, message = "调用失败", response = ApiResult.class)
    })
    @GetMapping(value = "role/getRole")
    public ApiResult getRole(Long roleId) {
        try {
            Role role = this.roleService.findRoleWithMenus(roleId);
            return ApiResult.ok(role);
        } catch (Exception e) {
            log.error("获取角色信息失败", e);
            return ApiResult.error("获取角色信息失败，请联系网站管理员！");
        }
    }

    @ApiOperation(value = "获取角色下的用户(分页)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "页数", required = true, dataType = "String", paramType = "query"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "调用成功", response = ApiResult.class),
            @ApiResponse(code = 500, message = "调用失败", response = ApiResult.class)
    })
    @GetMapping(value = "role/getPageUserWithRole")
    public ApiResult getPageUserWithRole(@ApiIgnore QueryRequest queryRequest, String roleId) {
        Map<String, Object> map = this.selectByPageNumSize(queryRequest, () -> this.roleService.getPageUserWithRole(roleId));
        return ApiResult.ok(map);
    }

    @ApiOperation(value = "修改保存角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "String", paramType = "query"),

    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "调用成功", response = ApiResult.class),
            @ApiResponse(code = 500, message = "调用失败", response = ApiResult.class)
    })
    @GetMapping(value = "role/editUpdate")
    public ApiResult editUpdate(@ApiIgnore Role role) {
        try {
            this.roleService.editUpdateRole(role);
            return ApiResult.ok("修改角色成功！");
        } catch (Exception e) {
            log.error("修改角色失败", e);
            return ApiResult.error("修改角色失败，请联系网站管理员！");
        }
    }

    @ApiOperation(value = "删除角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "角色ids(多选 逗号分隔)", required = true, dataType = "String", paramType = "query"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "调用成功", response = ApiResult.class),
            @ApiResponse(code = 500, message = "调用失败", response = ApiResult.class)
    })
    @GetMapping("role/delete")
    public ApiResult deleteRoles(String ids) {
        try {
            this.roleService.deleteRoles(ids);
            return ApiResult.ok("删除角色成功！");
        } catch (Exception e) {
            log.error("删除角色失败", e);
            return ApiResult.error("删除角色失败，请联系网站管理员！");
        }
    }

    @ApiOperation(value = "更新角色的菜单权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId", value = "菜单id(多选 逗号分隔)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "String", paramType = "query"),
    })
    @PostMapping(value = "role/update")
    public ApiResult updateRole(@ApiIgnore Role role, @ApiIgnore String menuId) {
        try {
            this.roleService.updateRole(role, menuId);
            return ApiResult.ok("修改菜单权限！");
        } catch (Exception e) {
            log.error("修改角色失败", e);
            return ApiResult.error("修改角色失败，请联系网站管理员！");
        }
    }

    /**
     * 操作权限
     **/

    @ApiOperation(value = "更新角色的菜单权限(单个)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId", value = "菜单id()", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型(1增加,2 删除)", required = true, dataType = "String", paramType = "query"),

    })
    @GetMapping(value = "role/updateSingalMenu")
    public ApiResult updateRole(@ApiIgnore String roleId, @ApiIgnore String menuId, @ApiIgnore String type) {
        return this.roleService.setUpdateRole(roleId, menuId, type);
    }


    @ApiOperation(value = "自定义角色增加")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = true, dataType = "String", paramType = "query"),
    })
    @PostMapping(value = "role/addCustomerRole")
    public ApiResult addCustomerRole(@ApiIgnore Role role) {
        role.setRoleType("1");
        return this.roleService.addCusRole(role);
    }

    @ApiOperation(value = "查询所有的自定义角色")
    @ApiResponses({
            @ApiResponse(code = 200, message = "调用成功", response = ApiResult.class),
            @ApiResponse(code = 500, message = "调用失败", response = ApiResult.class)
    })
    /**获取自定角色**/
    @GetMapping("role/getCustomerRole")
    public ApiResult getCustomerRole() {
        return this.roleService.getCustonRole();
    }

    @ApiOperation(value = "自定义角色增加人员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleid", value = "角色id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userid", value = "userid(不是username)", required = true, dataType = "String", paramType = "query"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "调用成功", response = ApiResult.class),
            @ApiResponse(code = 500, message = "调用失败", response = ApiResult.class)
    })
    @PostMapping(value = "role/addUserRoles")
    public ApiResult addUserRoles(String roleid, String userid) {
        return this.userRoleService.addUserRoles(roleid, userid);
    }

    @ApiOperation(value = "删除自定义角色得员工")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleid", value = "角色id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userid", value = "userid(不是username)", required = true, dataType = "String", paramType = "query"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "调用成功", response = ApiResult.class),
            @ApiResponse(code = 500, message = "调用失败", response = ApiResult.class)
    })
    @GetMapping(value = "role/deleteCusUserRoles")
    public ApiResult deleteCusUserRoles(String roleid, String userid) {
        try {
            return this.userRoleService.deleteCusRoleUserAndDept(roleid, userid);
        } catch (Exception e) {
            log.error("删除自定角色人员失败:" + e.getMessage());
        }
        return ApiResult.error("");
    }

    @ApiOperation(value = "设置自定义角色管理部门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "username(不是userid)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "deptid", value = "部门id(多个部门以,分隔)", required = true, dataType = "String", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "调用成功", response = ApiResult.class),
            @ApiResponse(code = 500, message = "调用失败", response = ApiResult.class)
    })
    @PostMapping(value = "role/setCusUserRoles2Dept")
    public ApiResult setCusUserRoles2Dept(@ApiIgnore UserWithDept dept) {
        try {
            return this.roleService.addCusRoleUser2Dept(dept);
        } catch (Exception e) {
            log.error("设置自定角色人员管理部门失败:" + e.getMessage());
        }
        return ApiResult.error("");
    }

}
