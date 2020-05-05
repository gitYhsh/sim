package com.xlcxx.plodes.system.dao;

import com.xlcxx.plodes.system.domain.MyUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 创建时间：2018年6月19日 下午5:11:28
 *
 * @author yhsh
 * @version 1.0
 * @since JDK 1.7.0_21
 * 类说明
 */
@Mapper
public interface DefPeopleDao {

    List<MyUser> selectDefPeople(@Param("typeId") String typeid);
}
