package com.xlcxx.plodes.system.service.impl;

import com.xlcxx.plodes.baseServices.impl.BaseServices;
import com.xlcxx.plodes.system.dao.UserRoleMapper;
import com.xlcxx.plodes.system.dao.UserWithDeptMapper;
import com.xlcxx.plodes.system.domain.UserRole;
import com.xlcxx.plodes.system.domain.UserWithDept;
import com.xlcxx.plodes.system.service.UserRoleService;
import com.xlcxx.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service("userRoleService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserRoleServiceImpl extends BaseServices<UserRole> implements UserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private UserWithDeptMapper userWithDeptMapper;

    @Override
    @Transactional
    public void deleteUserRolesByRoleId(String roleIds) {
        List<String> list = Arrays.asList(roleIds.split(","));
        this.batchDelete(list, "roleId", UserRole.class);
    }

    @Override
    @Transactional
    public void deleteUserRolesByUserId(String userIds) {
        List<String> list = Arrays.asList(userIds.split(","));
        this.batchDelete(list, "userId", UserRole.class);
    }

    @Override
    public void deleteUserRoleDeptByRoleId(String roleIds) {
        UserWithDept userWithDept = new UserWithDept();
        userWithDept.setRoleId(roleIds);
        userWithDeptMapper.delete(userWithDept);
    }

    @Override
    public ApiResult addUserRoles(String userRole, String userid) {
        try {
            List<UserRole> list = new ArrayList<>();
            String userids[] = userid.split(",");
            for (String users : userids) {
                UserRole userRole1 = new UserRole();
                userRole1.setRoleId(Long.valueOf(userRole));
                userRole1.setUserId(users);
                list.add(userRole1);
            }
            int num = this.userRoleMapper.insertList(list);
            return num > 0 ? ApiResult.ok("") : ApiResult.error("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResult.error("");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ApiResult deleteCusRoleUserAndDept(String roleId, String usersids) throws Exception {
        try {
            List<String> list = Arrays.asList(usersids.split(","));
            list.forEach(str -> {
                /**删除角色下得人员**/
                UserRole userRole = new UserRole();
                userRole.setUserId(str);
                userRole.setRoleId(Long.valueOf(roleId));
                int num = this.userRoleMapper.delete(userRole);
                if (num > 0) {
                    /**删除员工设置得部门**/
                    UserWithDept userWithDept = new UserWithDept();
                    userWithDept.setUserId(str);
                    userWithDept.setRoleId(roleId);
                    userWithDeptMapper.delete(userWithDept);
                }
            });

            return ApiResult.ok("");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public List<UserRole> getAllcharge(String roleid) {
        try {
            Example example = new Example(UserRole.class);
            example.createCriteria().andEqualTo("roleId", roleid);
            return this.selectByExample(example);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    @Override
    public String selectPowerDataByUserid(String userid) {
        try {
            Example example = new Example(UserRole.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andCondition("USER_ID=", userid);
            List<UserRole> tUserRoleList = this.selectByExample(example);
            if (tUserRoleList.size() > 0) {
                StringBuffer sb = new StringBuffer();
                for (UserRole powers : tUserRoleList) {
                    sb.append(powers.getRoleId() + ",");
                }
                String powerdata = sb.toString();
                String power = powerdata.substring(0, powerdata.lastIndexOf(","));
                return power;
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
