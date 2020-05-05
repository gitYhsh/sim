package com.xlcxx.plodes.system.service;

import com.xlcxx.plodes.baseServices.IServices;
import com.xlcxx.plodes.system.domain.TBasicSet;
import com.xlcxx.utils.ApiResult;

/**
 * Description: taskmanage
 * Created by yhsh on 2019/12/17 13:47
 * version 2.0
 * 方法说明
 */
public interface TbBasicServices extends IServices<TBasicSet> {

    /**
     * 查询基础设置的类型
     **/
    ApiResult getBasicConfig(String type);

    /**
     * 查询制度类型的子集
     **/
    ApiResult getBasicConfigSub(String pid);

    /**
     * 获取公司通式制度 规范
     **/
    ApiResult getZhiduTree(String type);


}
