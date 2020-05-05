package com.xlcxx.plodes.system.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlcxx.plodes.baseServices.impl.BaseServices;
import com.xlcxx.plodes.system.dao.RoleMapper;
import com.xlcxx.plodes.system.dao.RoleMenuMapper;
import com.xlcxx.plodes.system.dao.UserMapper;
import com.xlcxx.plodes.system.dao.UserWithDeptMapper;
import com.xlcxx.plodes.system.domain.*;
import com.xlcxx.plodes.system.service.DeptService;
import com.xlcxx.plodes.system.service.RoleMenuServie;
import com.xlcxx.plodes.system.service.RoleService;
import com.xlcxx.plodes.system.service.UserRoleService;
import com.xlcxx.utils.ApiResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service("roleService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RoleServiceImpl extends BaseServices<Role> implements RoleService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleMenuServie roleMenuService;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserWithDeptMapper userWithDeptMapper;
    @Autowired
    private DeptService deptService;

    @Override
    public ApiResult editUpdateRole(Role role) {
        try {
            int num = this.updateNotNull(role);
            return num > 0 ? ApiResult.ok("") : ApiResult.error("");
        } catch (Exception e) {
            log.error("修改名称失败:" + e.getMessage());
        }
        return ApiResult.error("");
    }

    @Override
    @Transactional
    public void addRole(Role role, Long[] menuIds) {
        this.save(role);
        setRoleMenus(role, menuIds);
    }

    private void setRoleMenus(Role role, Long[] menuIds) {
        Arrays.stream(menuIds).forEach(menuId -> {
            RoleMenu rm = new RoleMenu();
            rm.setMenuId(menuId);
            rm.setRoleId(role.getRoleId());
            this.roleMenuMapper.insert(rm);
        });
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void deleteRoles(String roleIds) {
        try {
            List<String> list = Arrays.asList(roleIds.split(","));
            this.batchDelete(list, "roleId", Role.class);
            this.roleMenuService.deleteRoleMenusByRoleId(roleIds);
            this.userRoleService.deleteUserRolesByRoleId(roleIds);
            this.userRoleService.deleteUserRoleDeptByRoleId(roleIds);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public RoleWithMenu findRoleWithMenus(Long roleId) {
        List<RoleWithMenu> list = this.roleMapper.findById(roleId);
        List<Long> menuList = new ArrayList<>();
        /**获取所有menu**/
        for (RoleWithMenu rwm : list) {
            if (!StringUtils.isEmpty(rwm.getMenuId() + "") && rwm.getMenuId() != null) {
                menuList.add(rwm.getMenuId());
            }
        }
        RoleWithMenu roleWithMenu = list.get(0);
        roleWithMenu.setMenuIds(menuList);
        List<MyUser> users = userMapper.findUserWithRoleId(roleId + "");
        JSONArray jsonArray = new JSONArray();
        for (MyUser user : users) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userid", user.getUserId());
            jsonObject.put("username", user.getUsername());
            jsonObject.put("nickname", user.getNickname());
            jsonObject.put("deptname", user.getDeptName());
            List<UserWithDept> listdepts = userWithDeptMapper.getUserWithDept(roleId + "", user.getUsername());
            jsonObject.put("setDepts", listdepts);
            jsonArray.add(jsonObject);
        }
        roleWithMenu.setMyUsers(jsonArray);
        return roleWithMenu;
    }

    @Override
    public List<MyUser> getPageUserWithRole(String roleId) {
        try {
            List<MyUser> users = userMapper.findUserWithRoleId(roleId);
            return users;
        } catch (Exception e) {
            log.error("获取角色下的用户失败:" + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    @Transactional
    public void updateRole(Role role, String menuIds) {
        Example example = new Example(RoleMenu.class);
        example.createCriteria().andCondition("role_id=", role.getRoleId());
        this.roleMenuMapper.deleteByExample(example);

        String args[] = menuIds.split(",");
        Long[] ints = new Long[args.length];
        for (int i = 0; i < args.length; i++) {
            ints[i] = Long.parseLong(args[i]);
        }
        setRoleMenus(role, ints);
    }

    @Override
    public ApiResult setUpdateRole(String roleIds, String menuid, String type) {
        try {
            if ("1".equals(type)) {
                //增加
                Example example = new Example(RoleMenu.class);
                example.createCriteria().andEqualTo("roleId", roleIds).andEqualTo("menuId", menuid);
                List<RoleMenu> list = roleMenuMapper.selectByExample(example);
                if (list.size() > 0) {
                    return ApiResult.error("当前角色已经有此权限");
                }
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setMenuId(Long.valueOf(menuid));
                roleMenu.setRoleId(Long.valueOf(roleIds));
                int num = roleMenuMapper.insert(roleMenu);
                return num > 0 ? ApiResult.ok("") : ApiResult.error("");
            } else {
                //删除权限
                Example example = new Example(RoleMenu.class);
                example.createCriteria().andEqualTo("roleId", roleIds).andEqualTo("menuId", menuid);
                int num = this.roleMenuMapper.deleteByExample(example);
                return num > 0 ? ApiResult.ok("") : ApiResult.error("");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("操作失败");
        }
        return ApiResult.error("");
    }

    /******************************************************************************/

    @Override
    public ApiResult addCusRole(Role role) {
        try {
            int num = this.save(role);
            return num > 0 ? ApiResult.ok(role.getRoleId()) : ApiResult.error("");
        } catch (Exception e) {
            log.error("增加自定义角色失败:" + e.getMessage());
        }
        return ApiResult.error("");
    }

    @Override
    public ApiResult getCustonRole() {
        try {
            Example example = new Example(Role.class);
            example.createCriteria().andEqualTo("roleType", "1");
            List<Role> list = this.selectByExample(example);
            return ApiResult.ok(list);
        } catch (Exception e) {
            log.error("获取自定义角色失败:" + e.getMessage());
        }
        return ApiResult.error("获取自定义失败");
    }

    @Override
    public ApiResult addCusRoleUser2Dept(UserWithDept userWithDept) {
        try {

            String depts = userWithDept.getDeptid();
            if (StringUtils.isEmpty(depts)) {
                Example example = new Example(UserWithDept.class);
                example.createCriteria().andEqualTo("roleId", userWithDept.getRoleId())
                        .andEqualTo("userId", userWithDept.getUserId());
                int num = userWithDeptMapper.deleteByExample(example);
                return num > 0 ? ApiResult.ok("设置成功") : ApiResult.error("设置失败");
            }

            List<String> listdept = Arrays.asList(depts.split(","));
            /***获取所有的一级部门****/
            List<String> deptids = new ArrayList<>();
            List<Dept> list = deptService.getOneLevelShowDept();
            for (Dept dept : list) {
                deptids.add(dept.getDeptId() + "");
            }
            for (String deptid : listdept) {
                Boolean ishave = deptids.contains(deptid);
                if (!ishave) {
                    return ApiResult.error("设置的部门包含不是一级部门");
                }
            }

            List<UserWithDept> listAll = new ArrayList<>();
            listdept.forEach(dept -> {
                UserWithDept dept1 = new UserWithDept();
                dept1.setRoleId(userWithDept.getRoleId());
                dept1.setUserId(userWithDept.getUserId());
                dept1.setDeptid(dept);
                listAll.add(dept1);
            });
            /**首先查询是否设置了该部门**/
            userWithDept.setDeptid(null);
            List<UserWithDept> deptsList = userWithDeptMapper.select(userWithDept);
            for (UserWithDept withDept : deptsList) {
                if (depts.contains(withDept.getDeptid())) {
                    return ApiResult.error("不能重复设置管理部门");
                }
            }

            if (!listAll.isEmpty()) {
                int num = userWithDeptMapper.insertList(listAll);
                return num > 0 ? ApiResult.ok("设置成功") : ApiResult.error("设置失败");
            }
        } catch (Exception e) {
            log.error("设置自定义角色失败:" + e.getMessage());
        }
        return ApiResult.error("设置自定义角色管理部门失败");
    }

    @Override
    public ApiResult getDefaultUser(String roleid) {
        try {


        } catch (Exception e) {
            log.error("获取默认人员角色失败:" + e.getMessage());
        }
        return null;
    }

}
