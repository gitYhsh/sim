package com.xlcxx.plodes.system.service;

import com.xlcxx.plodes.baseServices.IServices;
import com.xlcxx.plodes.system.domain.Menu;
import com.xlcxx.utils.ApiResult;
import com.xlcxx.utils.Tree;
import org.springframework.cache.annotation.CacheConfig;

import java.util.List;

@CacheConfig(cacheNames = "MenuService")
public interface MenuService extends IServices<Menu> {

    String findUserPermissions(String userName);

    List<Menu> findUserMenus(String userName);

    List<Menu> findAllMenus(Menu menu);

    Tree<Menu> getMenuButtonTree();

    List<Menu> getMenuTree();

    ApiResult getUserMenu(String userName);

    Menu findById(Long menuId);

    Menu findByNameAndType(String menuName, String type);

    void addMenu(Menu menu);

    void updateMenu(Menu menu);

    void deleteMeuns(String menuIds);

//    @Cacheable(key = "'url_'+ #p0")
//    List<Map<String, String>> getAllUrl(String p1);

    /**
     * 获取系统配置得权限设置
     **/
    List<Menu> findUserMenusSys(String userName, String parent);

    /**
     * 获取所有的菜单下的权限
     **/
    List<Menu> findMenuButtonByMenuIds(List<String> menus);

    /**
     * 查询个人和menuid 相关权限
     **/
    //@Cacheable(key = "'permissions_'+#p0+'username_'+#p1")
    List<String> findMenuButtonByUseridAndMenuId(String menuid, String username);

    /**
     * 获取子菜单
     **/
    List<Menu> findMenuSys(String menuid, String username);


    /**
     * 查询我的菜单
     **/
    List<Menu> selectMenuByUsername(String userid);

    List<Menu> selectLocalMenu();

}
