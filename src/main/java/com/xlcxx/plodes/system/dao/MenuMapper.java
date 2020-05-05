package com.xlcxx.plodes.system.dao;


import com.xlcxx.config.MyMapper;
import com.xlcxx.plodes.system.domain.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MenuMapper extends MyMapper<Menu> {

    List<Menu> findUserPermissions(String userName);

    List<Menu> findUserMenus(String userName);

    // 删除父节点，子节点变成顶级节点（根据实际业务调整）
    void changeToTop(List<String> menuIds);

    List<Menu> findUserMenusSys(@Param("userName") String userName, @Param("parent") String parent);


    // 删除父节点，子节点变成顶级节点（根据实际业务调整）


    List<Menu> selectMenuByUsername(@Param("userid") String userid);

    List<Menu> selectLocalMenu();
}

