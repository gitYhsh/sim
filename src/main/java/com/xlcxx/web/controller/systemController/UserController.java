package com.xlcxx.web.controller.systemController;


import com.xlcxx.config.aspect.ValifyToken;
import com.xlcxx.plodes.baseServices.RedisService;
import com.xlcxx.plodes.system.domain.Dept;
import com.xlcxx.plodes.system.domain.Menu;
import com.xlcxx.plodes.system.domain.MyUser;
import com.xlcxx.plodes.system.service.DeptService;
import com.xlcxx.plodes.system.service.MenuService;
import com.xlcxx.plodes.system.service.UserService;
import com.xlcxx.utils.*;
import com.xlcxx.web.controller.baseController.BaseController;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.*;


/**
 * 创建时间：2018年5月31日 下午3:18:22
 * 项目名称：taskmanage
 *
 * @author yhsh
 * @version 1.0
 * @since JDK 1.7.0_21
 * 类说明：   钉钉用户的一些操作
 */
@RestController
@Api(tags = "用户ApI")
public class UserController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private RedisService redis;
    @Autowired
    private DeptService deptService;
    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuServices;


    @ApiOperation(value = "增加人员(调用钉钉的接口拉取人员)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "钉钉的userid", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "nickname", value = "钉钉昵称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "deptId", value = "部门id", required = true, dataType = "String", paramType = "query"),
    })
    @GetMapping(value = "user/addUsers")
    public ApiResult addUsers(@ApiIgnore MyUser user) {
        try {
            return this.userService.addUserByDept(user);
        } catch (Exception e) {
            return ApiResult.error("增加用户失败:" + e.getMessage());
        }

    }

    @ApiOperation(value = "删除用户(批量删除用户)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userids", value = "批量删除userid(多个以,分割)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "quitReason", value = "离职原因", dataType = "String", paramType = "query"),
    })
    @PostMapping(value = "user/deleteUsers")
    public ApiResult deleteUsers(String userids, String quitReason) {
        if (StringUtils.isEmpty(userids)) {
            return ApiResult.error("传入参数失败");
        }
        try {
            return this.userService.batchUpdateUser(userids, quitReason);
        } catch (Exception e) {
        }
        return ApiResult.error("删除失败");
    }

    @ApiOperation(value = "调整用户部门(批量调整用户)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "username(多个以,分割)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "deptId", value = "部门id", required = true, dataType = "String", paramType = "query"),
    })
    @PostMapping(value = "user/adjustUser2Dept")
    public ApiResult adjustUser2Dept(String username, String deptId) {
        try {
            return this.userService.adjustUser2Dept(username, deptId);
        } catch (Exception e) {

        }
        return ApiResult.error("调整部门失败");
    }

    @ApiOperation(value = "获取jsAPI")
    @PostMapping(value = "user/getUserJsApi")
    public ApiResult getUserJsApi() {
        try {
            String jsapi_ticket = redis.get("jsapi_ticket");
            return ApiResult.ok(jsapi_ticket);
        } catch (Exception e) {
            logger.error("获取jsApi 失败：" + e.getMessage());
        }
        return ApiResult.error("");
    }



    @ApiOperation(value = "获取用户管理的一级部门(包含自定义角色和部门)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户userid(username)", required = true, dataType = "String", paramType = "query"),
    })
    @PostMapping(value = "user/getPersonManageDepts")
    public ApiResult getPersonManageDepts(String userid) {
        Map<String, String> map = redis.hmGetAll(userid);
        String permis = map.get("UserPermissions");
        List<String> permisArray = new ArrayList<>();
        if (!StringUtils.isEmpty(permis)) {
            permisArray = Arrays.asList(permis.split(","));
        }
        if (permisArray.contains(Permissions.ADMIN_VIEALL_DEPT)) {
            List<Dept> list = deptService.getOneLevelShowDept();
            return ApiResult.ok(list);
        }
        List<Dept> list = this.userService.getPersonManageDepts(userid);
        return ApiResult.ok(list);
    }

    @ApiOperation(value = "获取用户的一级部门(根据username 获取顶级部门信息)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "username(username)", required = true, dataType = "String", paramType = "query"),
    })
    @PostMapping(value = "user/getPersonDept")
    public ApiResult getPersonDept(String username) {
        return this.userService.getPersonDept(username);
    }

    @ApiOperation(value = "根据部门查人员列表（人员列表）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deptId", value = "部门id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "当前页数", required = true, dataType = "String", paramType = "query"),
    })
    @GetMapping(value = "user/getUserListByDeptid")
    public Map<String, Object> getUserListByDeptid(@ApiIgnore MyUser user, @ApiIgnore QueryRequest request) {
        return super.selectByPageNumSize(request, () -> userService.getUserListByDeptid(user, request));
    }
    /**
     * 获取用户菜单
     **/
    @ApiOperation(value = "获取用户菜单")
    @GetMapping(value = "userMenuByUsername")
    @ValifyToken
    public ApiResult userMenuByUsername1(@RequestParam("userid") String userid) {
        List<Menu> list = menuServices.selectMenuByUsername(userid);
        if (list.size() < 1) {
            //没有定义角色返回默认菜单
            return ApiResult.ok( menuServices.selectLocalMenu());
        } else {
            return ApiResult.ok(list);
        }
    }



}
