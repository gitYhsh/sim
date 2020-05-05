package com.xlcxx.plodes.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xlcxx.plodes.baseServices.RedisService;
import com.xlcxx.plodes.baseServices.impl.BaseServices;
import com.xlcxx.plodes.system.dao.DeptMapper;
import com.xlcxx.plodes.system.dao.UserMapper;
import com.xlcxx.plodes.system.dao.UserWithDeptMapper;
import com.xlcxx.plodes.system.domain.Dept;
import com.xlcxx.plodes.system.domain.MyUser;
import com.xlcxx.plodes.system.domain.UserWithDept;
import com.xlcxx.plodes.system.service.UserRoleService;
import com.xlcxx.plodes.system.service.UserService;
import com.xlcxx.utils.ApiResult;
import com.xlcxx.utils.Constant;
import com.xlcxx.utils.QueryRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserServicesImpl extends BaseServices<MyUser> implements UserService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DeptMapper deptMapper;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private UserWithDeptMapper userWithDeptMapper;

    @Autowired
    private RedisService redis;

    @Override
    public List<MyUser> getAllMyUser() {
        try {
            Example example = new Example(MyUser.class);
            example.createCriteria().andEqualTo("status", "0");
            return this.selectByExample(example);
        } catch (Exception e) {
            logger.error("获取所有的在职员工失败: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    /**
     * 逻辑删除用户
     **/
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ApiResult batchUpdateUser(String userIds, String quitReason) throws Exception {
        try {
            List<String> list = Arrays.asList(userIds.split(","));
            MyUser myUser = new MyUser();
            myUser.setStatus("1");
            int num = this.batchUpdate(list, "userId", MyUser.class, myUser);
            //删除用户的角色
            this.userRoleService.deleteUserRolesByUserId(userIds);
            //修改离职原因
            userMapper.updateByQuitReason(list, quitReason);
            return num > 0 ? ApiResult.ok("") : ApiResult.error("删除失败");
        } catch (Exception e) {
            logger.error("批量删除用户失败:" + e.getMessage());
            throw new RuntimeException();
        }
    }

    /**
     * 根据部门查员工
     */
    @Override
    public List<MyUser> selectUserByDeptid(String deptid) {
        try {
            Example example = new Example(MyUser.class);
            example.createCriteria().andEqualTo("deptId", deptid).andEqualTo("status", "0");
            return this.selectByExample(example);
        } catch (Exception e) {
            logger.error("获取人员失败：" + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public List<MyUser> findUserWithRoleId(String roleId) {
        try {
            return this.userMapper.findUserWithRoleId(roleId);
        } catch (Exception e) {
            logger.error("根据角色查询角色下的人员失败:" + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public List<MyUser> findUserWithDepts(List<String> deptid) {
        try {
            Example example = new Example(MyUser.class);
            example.createCriteria().andIn("deptId", deptid).andEqualTo("status", "0");
            return this.userMapper.selectByExample(example);
        } catch (Exception e) {
            logger.error("获取部门下的人员失败:" + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ApiResult addUserByDept(MyUser user) throws Exception {
        try {
            user.setUserId(user.getUsername());
            if (StringUtils.isEmpty(user.getUsername())) {
                return ApiResult.error("用户的id为空");
            }
            List<String> list = Arrays.asList(user.getUsername().split(","));
            Example example = new Example(MyUser.class);
            example.createCriteria().andIn("username", list);
            List<MyUser> users = this.selectByExample(example);
            if (!users.isEmpty()) {
                return ApiResult.error("增加的人员已经存在,请不要重复添加");
            }
            user.setStatus("0");
            int num = this.userMapper.insert(user);
           // userRoleService.addUserRoles(user.getUsername(), Constant.SYSTEM_PERSON_ID);
            return num > 0 ? ApiResult.ok("增加成功") : ApiResult.error("增加失败");
        } catch (Exception e) {
            logger.error("用户增加失败:" + e.getMessage());
            throw new RuntimeException("增加的钉钉用户默认的是普通员工");
        }
    }

    public MyUser findUserByUsername(String username) {
        try {
            Example example = new Example(MyUser.class);
            example.createCriteria().andEqualTo("username", username);
            MyUser user = this.selectByExample(example).get(0);
            return user != null ? user : null;
        } catch (Exception e) {
            logger.error("查询人员失败:" + e.getMessage());
            return null;
        }
    }

    @Override
    public String checkNickNames(String usernames) {
        try {
            String[] names = usernames.split(",");
            StringBuffer buffer = new StringBuffer();
            for (String name : names) {
                Example example = new Example(MyUser.class);
                example.createCriteria().andEqualTo("username", name);
                MyUser user = this.selectByExample(example).get(0);
                String nickname = user.getNickname();
                buffer.append(nickname + ",");
            }
            return buffer.toString();
        } catch (Exception e) {
            logger.error("处理多责任人失败:" + e.getMessage());
            return "";
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ApiResult adjustUser2Dept(String username, String deptid) throws Exception {
        try {

            /**参看用户是不是主管,如果是主管 则变成 空**/
            List<String> allUsers = Arrays.asList(username.split(","));
            for (String userna : allUsers) {
                Example example = new Example(MyUser.class);
                example.createCriteria().andEqualTo("username", userna);
                MyUser user = this.selectByExample(example).get(0);

                Example exampleDept = new Example(Dept.class);
                exampleDept.createCriteria().andEqualTo("deptId", user.getDeptId());
                Dept dept = deptMapper.selectByExample(exampleDept).get(0);
                if (!StringUtils.isEmpty(dept.getDeptDirector())) {
                    if (dept.getDeptDirector().contains(userna)) {
                        String deptAdmin = dept.getDeptDirector().replace(userna, "");
                        dept.setDeptDirector(deptAdmin);
                    }
                }
                user.setDeptId(Long.parseLong(deptid));
                /**更新用户**/
                this.updateNotNull(user);
                this.deptMapper.updateByPrimaryKey(dept);
            }
            return ApiResult.ok("修改成功");
        } catch (Exception e) {
            logger.error("人员调整部门失败:" + e.getMessage());
            throw new RuntimeException();
        }
    }

    @Override
    public ApiResult adjustUser2Station(String userid, String station) {
        try {
            if (StringUtils.isEmpty(userid)) {
                return ApiResult.error("接收参数失败");
            }
            if (StringUtils.isEmpty(station)) {
                station = null;
            }
            //是不是允许 在岗人员调整岗位? TODO
            MyUser user = this.userMapper.selectByPrimaryKey(Long.parseLong(userid));
            user.setStation(station);
            int num = this.updateAll(user);
            return num > 0 ? ApiResult.ok("调整岗位成功") : ApiResult.error("调整岗位失败");
        } catch (Exception e) {
            logger.error("调整岗位失败:" + e.getMessage());
        }
        return ApiResult.error("修改失败");
    }

    @Override
    public ApiResult getStationUser(String station) {
        try {
            List<MyUser> users = this.userMapper.selectUser2StationDept(station);
            return ApiResult.ok(users);
        } catch (Exception e) {
            logger.error("获取岗位下的人员失败: " + e.getMessage());
        }
        return ApiResult.error("获取岗位人员失败");
    }

    @Override
    public List<Dept> getPersonManageDepts(String userid) {
        try {
            Set<String> deptids = new HashSet<>();
            /**查询个人部门**/
            MyUser user = new MyUser();
            user.setUserId(userid);
            user.setStatus("0");
            MyUser user1 = this.userMapper.selectOne(user);
            String deptid = pasrseOneLevelDept(user1.getDeptId() + "");
            deptids.add(deptid);
            /**查询管理部门**/
            UserWithDept userWithDept = new UserWithDept();
            userWithDept.setUserId(userid);
            List<UserWithDept> list = userWithDeptMapper.select(userWithDept);
            for (UserWithDept dept : list) {
                deptids.add(dept.getDeptid());
            }

            Example example = new Example(Dept.class);
            example.createCriteria().andEqualTo("deptType", "1").andEqualTo("deptDel", "0")
                    .andIn("deptId", deptids);
            example.setOrderByClause("dept_creattime desc");
            return deptMapper.selectByExample(example);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取个人管理的部门失败:" + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public ApiResult getPersonDept(String username) {
        try {
            Dept dept = null;
            MyUser user = new MyUser();
            user.setUsername(username);
            user.setStatus("0");
            MyUser user1 = this.userMapper.selectOne(user);
            String deptid = pasrseOneLevelDept(user1.getDeptId() + "");
            if (!StringUtils.isEmpty(deptid)) {
                dept = deptMapper.selectByPrimaryKey(Long.parseLong(deptid));
                return ApiResult.ok(dept);
            }
        } catch (Exception e) {
            logger.error("获取用户一级部门失败:" + e.getMessage());
        }
        return ApiResult.error("当前用户没有一级部门,请先去系统设置一级部门");
    }

    private String pasrseOneLevelDept(String deptid) {
        if (deptid.equals("1") || deptid.equals("0")) {
            return "";
        }
        Dept dept = deptMapper.selectByPrimaryKey(Long.parseLong(deptid));
        if ("0".equals(dept.getDeptDel()) && "1".equals(dept.getDeptType())) {
            return dept.getDeptId() + "";
        }
        return pasrseOneLevelDept(dept.getParentId() + "");
    }

    @Override
    public MyUser getUserDetail(String username) {
        try {
            MyUser user = new MyUser();
            user.setUsername(username);
            return userMapper.findUserWithDept(user).get(0);
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    public List<MyUser> getUserListByDeptid(MyUser user, QueryRequest request) {
        try {
            Example example = new Example(MyUser.class);
            example.createCriteria().andEqualTo("deptId", user.getDeptId()).andEqualTo("status", "0");
            List<MyUser> list = this.selectByExample(example);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<MyUser> getAllUser(String username) {
        try {
            return this.selectAll();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取用户列表失败：" + e.getMessage());
        }
        return new ArrayList<>();
    }
}
