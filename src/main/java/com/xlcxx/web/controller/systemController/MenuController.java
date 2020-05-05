package com.xlcxx.web.controller.systemController;

import com.xlcxx.plodes.system.domain.Menu;
import com.xlcxx.plodes.system.service.MenuService;
import com.xlcxx.utils.ApiResult;
import com.xlcxx.utils.Tree;
import com.xlcxx.web.controller.baseController.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "系统菜单")
public class MenuController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MenuService menuService;

    @ApiOperation(value = "根据用户名获取菜单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "query"),
    })

    @GetMapping(value = "menu/menu")
    public ApiResult getMenu(String userName) {
        try {
            List<Menu> menus = this.menuService.findUserMenus(userName);
            return ApiResult.ok(menus);
        } catch (Exception e) {
            logger.error("获取菜单失败", e);
            return ApiResult.error("获取菜单失败！");
        }
    }

    @ApiOperation(value = "获取菜单栏")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId", value = "菜单id", required = true, dataType = "String", paramType = "query"),
    })

    @GetMapping(value = "menu/getMenu")
    public ApiResult getMenu(Long menuId) {
        try {
            Menu menu = this.menuService.findById(menuId);
            return ApiResult.ok(menu);
        } catch (Exception e) {
            logger.error("获取菜单信息失败", e);
            return ApiResult.error("获取信息失败，请联系网站管理员！");
        }
    }

    @ApiOperation(value = "获取菜单按钮树")

    @GetMapping("menu/menuButtonTree")
    public ApiResult getMenuButtonTree() {
        try {
            Tree<Menu> tree = this.menuService.getMenuButtonTree();
            return ApiResult.ok(tree);
        } catch (Exception e) {
            logger.error("获取菜单列表失败", e);
            return ApiResult.error("获取菜单列表失败！");
        }
    }

    @ApiOperation(value = "获取菜单树")
    @GetMapping(value = "menu/tree")
    public ApiResult getMenuTree() {
        try {
            List<Menu> tree = this.menuService.getMenuTree();
            return ApiResult.ok(tree);
        } catch (Exception e) {
            logger.error("获取菜单树失败", e);
            return ApiResult.error("");
        }

    }

    @ApiOperation(value = "根据用户名获取菜单树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "query"),
    })
    @GetMapping("menu/getUserMenu")
    public ApiResult getUserMenu(String userName) {
        try {
            return this.menuService.getUserMenu(userName);
        } catch (Exception e) {
            logger.error("获取用户菜单失败", e);
            return ApiResult.error("获取用户菜单失败！");
        }
    }

    @GetMapping("menu/getSubMenuidMenu")
    public ApiResult getSubMenuidByParentid(String menuid) {
        List<Menu> list = this.menuService.findMenuSys(menuid, "");
        return ApiResult.ok(list);
    }


}
