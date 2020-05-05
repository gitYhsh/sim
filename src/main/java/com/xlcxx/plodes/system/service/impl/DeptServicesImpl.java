package com.xlcxx.plodes.system.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlcxx.plodes.baseServices.impl.BaseServices;

import com.xlcxx.plodes.system.dao.DeptMapper;
import com.xlcxx.plodes.system.dao.RoleMapper;
import com.xlcxx.plodes.system.domain.Dept;
import com.xlcxx.plodes.system.domain.MyUser;
import com.xlcxx.plodes.system.service.DeptService;
import com.xlcxx.plodes.system.service.UserService;
import com.xlcxx.utils.ApiResult;
import com.xlcxx.utils.Constant;
import com.xlcxx.utils.DateUtil;
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


@Service
@Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
public class DeptServicesImpl extends BaseServices<Dept> implements DeptService {

    private static final Logger logger = LoggerFactory.getLogger(DeptServicesImpl.class);

    @Autowired
    private UserService userService;
    @Autowired
    private DeptMapper deptMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public ApiResult getDeptOrgInfo2Tree(String deptid) {
        JSONArray jsonArrayDept = new JSONArray();
        JSONObject jsonObject = new JSONObject(true);
        try {
            /**获取部门信息**/
            Dept dept = deptMapper.selectByPrimaryKey(Long.parseLong(deptid));
            jsonObject.put("deptId", deptid);
            jsonObject.put("name", dept.getDeptName());

            /**获取当前部门的人**/
            List<MyUser> users = userService.selectUserByDeptid(deptid);
            /**判断是不是主管**/
            final String deptMange = dept.getDeptDirector();
            JSONArray jsonArray = new JSONArray();
            users.forEach(myUser -> {
                JSONObject userjson = new JSONObject();
                userjson.put("name", myUser.getNickname());
                userjson.put("username", myUser.getUsername());
                if (!StringUtils.isEmpty(deptMange)) {
                    boolean ble = deptMange.contains(myUser.getUsername());
                    userjson.put("advor", ble);
                } else {
                    userjson.put("advor", false);
                }
                userjson.put("userid", myUser.getUserId() + "");
                jsonArray.add(userjson);
            });
            jsonObject.put("user", jsonArray);

            /**获取当前子部门**/
            Example example = new Example(Dept.class);
            example.createCriteria().andEqualTo("parentId", deptid).andEqualTo("deptDel", "0");
            List<Dept> listdept = this.selectByExample(example);
            JSONArray deptjson = new JSONArray();

            List<String> listr = new ArrayList<>();
            List<String> list = getChiled(deptid, listr);
            list.add(deptid);
            List<MyUser> usersDept = userService.findUserWithDepts(list);
            jsonObject.put("deptnum", usersDept.size());
            jsonObject.put("deptUser", usersDept);
            listdept.forEach(deptchiled -> {
                JSONObject deptArry = new JSONObject();
                deptArry.put("name", deptchiled.getDeptName());
                deptArry.put("id", deptchiled.getDeptId());
                List<String> listdhiled = new ArrayList<>();
                List<String> listchiled = getChiled(deptchiled.getDeptId() + "", listdhiled);
                listdhiled.add(deptchiled.getDeptId() + "");
                List<MyUser> usersDeptChiled = userService.findUserWithDepts(listchiled);
                deptArry.put("deptnum", usersDeptChiled.size());
                deptArry.put("deptUser", usersDeptChiled);
                deptjson.add(deptArry);
            });
            jsonObject.put("dept", deptjson);
            jsonArrayDept.add(jsonObject);
            return ApiResult.ok(jsonArrayDept);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取组织结构失败:" + e.getMessage());
            e.printStackTrace();
        }
        return ApiResult.error("获取组织结构失败");
    }

    /**
     * 递归获取子部门的人数
     **/
    private List<String> getChiled(String deptid, List<String> list) {
        Example example = new Example(Dept.class);
        example.createCriteria().andEqualTo("parentId", deptid).andEqualTo("deptDel", "0");
        example.setOrderByClause("dept_creattime desc");
        List<Dept> listdept = this.selectByExample(example);
        for (Dept dept : listdept) {
            list.add(dept.getDeptId() + "");
            getChiled(dept.getDeptId() + "", list);
        }
        return list;
    }

    @Override
    public ApiResult getDeptOrge(String deptid) {
        try {
            JSONArray jsonArrayDept = new JSONArray();
            JSONObject jsonObject = new JSONObject(true);
            /**获取部门信息**/
            Dept dept = deptMapper.selectByPrimaryKey(Long.parseLong(deptid));

            /**获取部门下的人**/
            List<String> listr = new ArrayList<>();
            List<String> list = getChiled(deptid, listr);
            list.add(deptid);
            List<MyUser> usersDept = userService.findUserWithDepts(list);
            jsonObject.put("id", deptid);
            jsonObject.put("title", dept.getDeptName() + "(" + usersDept.size() + " 人)");
            /**子部门**/
            jsonObject.put("children", getChiledDept(deptid, jsonArrayDept));
            jsonObject.put("spread", true);
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(jsonObject);
            return ApiResult.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResult.error("");
    }

    @Override
    public ApiResult getDeptTree(String deptid) {
        JSONArray jsonArray1 = new JSONArray();
        try {
            JSONObject jsonObject = new JSONObject(true);
            /**获取部门信息**/
            Dept dept = deptMapper.selectByPrimaryKey(Long.parseLong(deptid));
            jsonObject.put("id", deptid);
            jsonObject.put("title", dept.getDeptName());
            jsonObject.put("children", getTreeDept(deptid, jsonArray1));
            jsonObject.put("spread", true);
            jsonArray1.add(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResult.ok(jsonArray1);
    }

    /**
     * 递归获取子部门
     **/
    private JSONArray getTreeDept(String deptids, JSONArray jsonArray) {
        /**获取当前子部门**/
        Example example = new Example(Dept.class);
        example.createCriteria().andEqualTo("parentId", deptids).andEqualTo("deptDel", "0");
        List<Dept> listdept = this.selectByExample(example);

        JSONArray jsonArray1 = new JSONArray();
        listdept.forEach(subDept -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", subDept.getDeptId());
            jsonObject.put("title", subDept.getDeptName());
            jsonObject.put("children", getTreeDept(subDept.getDeptId() + "", jsonArray1));
            jsonArray1.add(jsonObject);
        });
        return jsonArray1;
    }

    /**
     * 递归获取子部门
     **/
    private JSONArray getChiledDept(String deptids, JSONArray jsonArray) {
        /**获取当前子部门**/
        JSONArray jsonArray1 = new JSONArray();
        Example example = new Example(Dept.class);
        example.createCriteria().andEqualTo("parentId", deptids).andEqualTo("deptDel", "0");
        List<Dept> listdept = this.selectByExample(example);
        listdept.forEach(subDept -> {
            JSONObject jsonObject = new JSONObject();
            List<String> listr = new ArrayList<>();
            List<String> list = getChiled(subDept.getDeptId() + "", listr);
            list.add(subDept.getDeptId() + "");
            List<MyUser> usersSubDept = userService.findUserWithDepts(list);
            jsonObject.put("id", subDept.getDeptId());
            jsonObject.put("title", subDept.getDeptName() + "(" + usersSubDept.size() + " 人)");
            jsonObject.put("children", getChiledDept(subDept.getDeptId() + "", jsonArray1));
            jsonArray1.add(jsonObject);
        });
        return jsonArray1;
    }

    @Override
    public ApiResult getDeptInfo(String deptid) {
        try {
            Dept deptSub = this.deptMapper.selectByPrimaryKey(Long.parseLong(deptid));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deptId", deptSub.getDeptId());
            jsonObject.put("deptName", deptSub.getDeptName());
            jsonObject.put("parentId", deptSub.getParentId());
            /**获取父级部门**/
            Dept deptParent = this.deptMapper.selectByPrimaryKey(deptSub.getParentId());
            if (deptParent != null) {
                jsonObject.put("parentName", deptParent.getDeptName());
            } else {
                jsonObject.put("parentName", "");
            }
            jsonObject.put("isShow", deptSub.getDeptType());
            /**获取部门主管**/
            JSONArray jsonArray = new JSONArray();
            String manage = deptSub.getDeptDirector();
            if (!StringUtils.isEmpty(manage)) {
                List<MyUser> list = userService.selectAll();
                list.forEach(myuser -> {
                    if (manage.contains(myuser.getUsername())) {
                        JSONObject object = new JSONObject();
                        object.put("userid", myuser.getUserId());
                        object.put("username", myuser.getUsername());
                        object.put("nickname", myuser.getNickname());
                        jsonArray.add(object);
                    }
                });
            }
            jsonObject.put("admins", jsonArray);
            return ApiResult.ok(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取部门详情失败: " + e.getMessage());
        }
        return ApiResult.error("获取部门详情失败");
    }

    /**
     * 修改部门的基本信息
     **/
    @Override
    public ApiResult updateDeptInfo(Dept dept) {
        try {
            Dept dept1 = this.deptMapper.selectByPrimaryKey(dept.getDeptId());
            String type = dept1.getDeptStatus();
            if ("1".equals(type)) return ApiResult.error("初始化部门不能修改");
            /**更新部门标题**/
            if (StringUtils.isEmpty(dept.getDeptName())) {
                return ApiResult.error("部门名称不能为空");
            }

            /**更新管理员**/
            if (!StringUtils.isEmpty(dept.getDeptDirector())) {
                /**查看是不是 主管角色，如果不是，则不能设置**/
                List<String> admins = Arrays.asList(dept.getDeptDirector().split(","));
                List<MyUser> users = this.roleMapper.getDefaultRoleUser(Constant.SYSTEM_ZHUGUAN_ID);
                List<String> allAdmins = new ArrayList<>();
                for (MyUser user : users) {
                    allAdmins.add(user.getUsername());
                }
                String allUsers = StringUtils.join(allAdmins, ",");
                for (String admin : admins) {
                    if (!allUsers.contains(admin)) {
                        return ApiResult.error("当前设置的人的角色不是主管");
                    }
                }
            }
            /**更新是否显示**/
            dept1.setDeptType(dept.getDeptType());
            /**更新上级部门**/
            dept1.setParentId(dept.getParentId());
            /**更新名字**/
            dept1.setDeptName(dept.getDeptName());
            /**修改主管**/
            dept1.setDeptDirector(dept.getDeptDirector());
            int num = this.updateAll(dept1);
            return num > 0 ? ApiResult.ok("修改成功") : ApiResult.error("修改失败");
        } catch (Exception e) {
            logger.error("修改部门信息失败:" + e.getMessage());
        }
        return ApiResult.error("修改失败");
    }

    @Override
    public ApiResult addSubDeptInfo(Dept dept) {
        try {
            dept.setDeptStatus("0");
            dept.setDeptType("0");
            dept.setDeptDel("0");
            dept.setDeptCreattime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:dd"));
            int num = this.save(dept);
            return num > 0 ? ApiResult.ok("增加成功") : ApiResult.error("增加失败");
        } catch (Exception e) {
            logger.error("增加子部门失败:" + e.getMessage());
        }
        return ApiResult.error("增加失败");
    }

    @Override
    public ApiResult deleteDept(Dept dept) {
        try {
            Dept dept1 = this.deptMapper.selectByPrimaryKey(dept.getDeptId());
            String type = dept1.getDeptStatus();
            if ("1".equals(type)) return ApiResult.error("初始化部门不能删除");
            Example example = new Example(Dept.class);
            example.createCriteria().andEqualTo("parentId", dept1.getDeptId()).andEqualTo("deptDel", "0");
            List<Dept> list = this.selectByExample(example);
            if (!list.isEmpty()) {
                return ApiResult.error("该部门下存在子部门,请先删除子部门");
            }

            List<MyUser> users = userService.selectUserByDeptid(dept1.getDeptId() + "");
            if (!users.isEmpty()) {
                return ApiResult.error("该部门下存在员工,请先删除人员");
            }
            dept1.setDeptDel("1");
            int num = this.updateNotNull(dept1);
            return num > 0 ? ApiResult.ok("删除成功") : ApiResult.error("删除失败");
        } catch (Exception e) {
            logger.error("删除部门失败:" + e.getMessage());
        }
        return ApiResult.ok("删除失败");
    }

    @Override
    public Dept findDeptById(String deptid) {
        try {
            Example example = new Example(Dept.class);
            example.createCriteria().andEqualTo("deptId", deptid);
            Dept dept = this.selectByExample(example).get(0);
            return dept != null ? dept : null;
        } catch (Exception e) {
            logger.error("查询部门失败:" + e.getMessage());
            return null;
        }
    }

    @Override
    public ApiResult getDeptByParentid(String deptid) {
        try {
            Example example = new Example(Dept.class);
            example.createCriteria().andEqualTo("parentId", deptid).andEqualTo("deptDel", "0");
            List<Dept> list = this.selectByExample(example);
            return ApiResult.ok(list);
        } catch (Exception e) {
            logger.error("查询部门的子部门：" + e.getMessage());
        }
        return ApiResult.ok("查询部门的子部门失败");
    }

    @Override
    public List<Dept> getOneLevelShowDept() {
        try {
            Example example = new Example(Dept.class);
            example.createCriteria().andEqualTo("deptType", "1").andEqualTo("deptDel", "0");
            example.setOrderByClause("dept_creattime desc");
            return this.selectByExample(example);
        } catch (Exception e) {
            logger.error("获取到的一级显示部门失败：" + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public ApiResult getSelectUsers(String parentid, String curentid) {
        try {
            String deptids = "";
            Example example = new Example(Dept.class);
            Example.Criteria criteria = example.createCriteria();
            if (StringUtils.isEmpty(parentid)) {
                criteria.andEqualTo("parentId", curentid);
                deptids = curentid;
            }
            if (StringUtils.isEmpty(curentid)) {
                criteria.andEqualTo("parentId", parentid);
                deptids = parentid;
            }
            List<Dept> depts = this.selectByExample(example);

            List<MyUser> users = userService.selectUserByDeptid(deptids);
            JSONObject jsonObject = new JSONObject();

            Dept dept = deptMapper.selectByPrimaryKey(Long.parseLong(deptids));
            jsonObject.put("deptpid", dept.getParentId());
            jsonObject.put("depts", depts);
            jsonObject.put("users", users);
            return ApiResult.ok(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取部门组织架构失败:" + e.getMessage());
        }
        return ApiResult.error("获取组织接口失败");
    }
}
