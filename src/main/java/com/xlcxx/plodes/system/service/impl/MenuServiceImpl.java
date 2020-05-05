package com.xlcxx.plodes.system.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlcxx.plodes.baseServices.impl.BaseServices;
import com.xlcxx.plodes.system.dao.MenuMapper;
import com.xlcxx.plodes.system.domain.Menu;
import com.xlcxx.plodes.system.service.MenuService;
import com.xlcxx.plodes.system.service.RoleMenuServie;
import com.xlcxx.utils.ApiResult;
import com.xlcxx.utils.MenuTree;
import com.xlcxx.utils.Tree;
import com.xlcxx.utils.TreeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.*;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MenuServiceImpl extends BaseServices<Menu> implements MenuService {

    private Logger log = LoggerFactory.getLogger(MenuServiceImpl.class);


    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMenuServie roleMenuService;

    @Override
    public String findUserPermissions(String userName) {
        List<Menu> list = this.menuMapper.findUserPermissions(userName);
        Set<String> seths = new HashSet<>();
        for (Menu menu : list) {
            String perms = menu.getPerms();
            seths.add(perms);
        }
        return StringUtils.join(seths, ",");
    }

    @Override
    public List<Menu> findUserMenus(String userName) {
        return this.menuMapper.findUserMenus(userName);
    }

    @Override
    public List<Menu> findUserMenusSys(String userName, String parent) {
        return this.menuMapper.findUserMenusSys(userName, parent);
    }

    @Override
    public List<Menu> findMenuButtonByMenuIds(List<String> menus) {
        try {
            Example example = new Example(Menu.class);
            example.createCriteria().andIn("parentId", menus).andEqualTo("type", "1");
            return this.selectByExample(example);
        } catch (Exception e) {
            log.error("获取菜单下的权限失败:" + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public List<Menu> findAllMenus(Menu menu) {
        try {
            Example example = new Example(Menu.class);
            Criteria criteria = example.createCriteria();
            if (StringUtils.isNotBlank(menu.getMenuName())) {
                criteria.andCondition("menu_name=", menu.getMenuName());
            }
            if (StringUtils.isNotBlank(menu.getType())) {
                criteria.andCondition("type=", Long.valueOf(menu.getType()));
            }
            if (menu.getParentId() != null) {
                criteria.andCondition("parent_id=", menu.getParentId() + "");
            }
            example.setOrderByClause("menu_id");
            return this.selectByExample(example);
        } catch (NumberFormatException e) {
            log.error("error", e);
            return new ArrayList<>();
        }
    }

    @Override
    public Tree<Menu> getMenuButtonTree() {
        List<Tree<Menu>> trees = new ArrayList<>();
        List<Menu> menus = this.findAllMenus(new Menu());
        buildTrees(trees, menus);
        return TreeUtils.build(trees);
    }

    @Override
    public List<Menu> getMenuTree() {
        Example example = new Example(Menu.class);
        example.createCriteria();
        example.setOrderByClause("create_time");
        List<Menu> menus = this.selectByExample(example);
        return menus;
    }

    private JSONArray getTreeMenu(String pid, JSONArray jsonArray) {
        /**获取当前子部门**/
        Example example = new Example(Menu.class);
        example.createCriteria().andEqualTo("parentId", pid);
        List<Menu> listMenu = this.selectByExample(example);

        JSONArray jsonArray1 = new JSONArray();
        listMenu.forEach(subMenu -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", subMenu.getMenuId());
            jsonObject.put("title", subMenu.getMenuName());
            jsonObject.put("children", getTreeMenu(subMenu.getMenuId() + "", jsonArray1));
            jsonArray1.add(jsonObject);
        });
        return jsonArray1;
    }


    private void buildTrees(List<Tree<Menu>> trees, List<Menu> menus) {
        menus.forEach(menu -> {
            Tree<Menu> tree = new Tree<>();
            tree.setId(menu.getMenuId().toString());
            tree.setParentId(menu.getParentId().toString());
            tree.setText(menu.getMenuName());
            trees.add(tree);
        });
    }

    @Override
    public ApiResult getUserMenu(String userName) {
        List<MenuTree<Menu>> trees = new ArrayList<>();
        List<Menu> menus = this.findUserMenus(userName);
        menus.forEach(menu -> {
            MenuTree<Menu> tree = new MenuTree<>();
            tree.setId(menu.getMenuId().toString());
            tree.setParentId(menu.getParentId().toString());
            tree.setLabel(menu.getMenuName());
            tree.setIcon(menu.getIcon());
            tree.setComponent(menu.getUrl());
            tree.setPath(menu.getPath());
            tree.setHead(Integer.parseInt(menu.getOrderNum() + ""));
            trees.add(tree);
        });
        List<MenuTree<Menu>> topNodes = new ArrayList<>();
        trees.forEach(children -> {
            String pid = children.getParentId();
            if (pid == null || "0".equals(pid)) {
                topNodes.add(children);
                return;
            }
            for (MenuTree<Menu> parent : trees) {
                String id = parent.getId();
                if (id != null && id.equals(pid)) {
                    parent.getChildren().add(children);
                    return;
                }
            }
        });
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("menus", topNodes);
        return ApiResult.ok(jsonObject);
    }

    @Override
    public Menu findByNameAndType(String menuName, String type) {
        Example example = new Example(Menu.class);
        example.createCriteria().andCondition("lower(menu_name)=", menuName.toLowerCase())
                .andEqualTo("type", Long.valueOf(type));
        List<Menu> list = this.selectByExample(example);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    @Transactional
    public void addMenu(Menu menu) {
        menu.setCreateTime(new Date());
        if (menu.getParentId() == null)
            menu.setParentId(0L);
        if (Menu.TYPE_BUTTON.equals(menu.getType())) {
            menu.setUrl(null);
            menu.setIcon(null);
        }
        this.save(menu);
    }

    @Override
    @Transactional
    public void deleteMeuns(String menuIds) {
        List<String> list = Arrays.asList(menuIds.split(","));
        this.batchDelete(list, "menuId", Menu.class);
        this.roleMenuService.deleteRoleMenusByMenuId(menuIds);
        this.menuMapper.changeToTop(list);
    }

    @Override
    public Menu findById(Long menuId) {
        return this.selectByKey(menuId);
    }

    @Override
    @Transactional
    public void updateMenu(Menu menu) {
        menu.setModifyTime(new Date());
        if (menu.getParentId() == null)
            menu.setParentId(0L);
        if (Menu.TYPE_BUTTON.equals(menu.getType())) {
            menu.setUrl(null);
            menu.setIcon(null);
        }
        this.updateNotNull(menu);
    }

    @Override
    public List<String> findMenuButtonByUseridAndMenuId(String menuId, String username) {
        try {
            /**获取此用户的一级菜单**/
            List<Menu> list = this.findUserMenus(username);
            List<String> allMenu = new ArrayList<>();
            list.forEach(menu -> allMenu.add(menu.getMenuId() + ""));
            List<String> menuids = new ArrayList<>();
            if (StringUtils.isEmpty(menuId)) {
                /**如果menuid 为空 则获取所有的权限码**/
                menuids.addAll(allMenu);
            } else {
                menuids.add(menuId);
            }
            List<Menu> buttonMenu = this.findMenuButtonByMenuIds(menuids);
            List<String> permissions = new ArrayList<>();
            /**查询所有用户的权限**/
            String allUserPersion = this.findUserPermissions(username);
            List<String> allPersion = Arrays.asList(allUserPersion.split(","));
            buttonMenu.forEach(menu -> {
                if (allPersion.contains(menu.getPerms())) {
                    permissions.add(menu.getPerms());
                }
            });
            return permissions;
        } catch (Exception e) {
            log.error("查询个人菜单权限码失败：" + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    //TODO(根据用户获取配置权限)
    public List<Menu> findMenuSys(String menuid, String username) {
        try {
            Example example = new Example(Menu.class);
            example.createCriteria().andEqualTo("parentId", menuid).andEqualTo("type", "0");
            return this.selectByExample(example);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    @Override
    public List<Menu> selectMenuByUsername(String userid) {
        List<Menu> menude = new ArrayList<>();
        try {
            List<Menu> tMenus = menuMapper.selectMenuByUsername(userid);
            for (Menu menu : tMenus) {
                List<Menu> chiled = new ArrayList<>();
                for (Menu menu1 : tMenus) {
                    if (menu1.getParentId().equals(menu.getMenuId())) {
                        chiled.add(menu1);
                    }
                }
                menu.setChilds(chiled);
                if ("0".equals(menu.getParentId()+"")) {
                    menude.add(menu);
                }
            }
            return menude;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取个人菜单失败:" + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public List<Menu> selectLocalMenu() {
        List<Menu> menude = new ArrayList<>();
        try {
            List<Menu> menus = menuMapper.selectLocalMenu();
            for (Menu menu : menus) {
                List<Menu> chiled = new ArrayList<>();
                for (Menu menu1 : menus) {
                    if (menu1.getParentId().equals(menu.getMenuId())) {
                        chiled.add(menu1);
                    }
                }
                menu.setChilds(chiled);
                if ("0".equals(String.valueOf(menu.getParentId()))) {
                    menude.add(menu);
                }
            }
            return menude;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取个人菜单失败:" + e.getMessage());
        }
        return new ArrayList<>();
    }
}
