package com.xlcxx.plodes.system.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlcxx.plodes.baseServices.impl.BaseServices;
import com.xlcxx.plodes.system.domain.TBasicSet;
import com.xlcxx.plodes.system.service.TbBasicServices;
import com.xlcxx.utils.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Description: taskmanage
 * Created by yhsh on 2019/12/17 13:52
 * version 2.0
 * 方法说明  基础配置
 */
@Service
public class TbBasicServicesImpl extends BaseServices<TBasicSet> implements TbBasicServices {

    private static final Logger logger = LoggerFactory.getLogger(TbBasicServicesImpl.class);

    @Override
    public ApiResult getBasicConfig(String type) {
        try {
            Example example = new Example(TBasicSet.class);
            example.createCriteria().andEqualTo("tbType", type);
            List<TBasicSet> list = this.selectByExample(example);
            return ApiResult.ok(list);
        } catch (Exception e) {
            logger.error("获取基础配置的信息失败" + e.getMessage());
        }
        return ApiResult.error("");
    }

    @Override
    public ApiResult getBasicConfigSub(String pid) {
        try {
            Example example = new Example(TBasicSet.class);
            example.createCriteria().andEqualTo("tbPid", pid);
            List<TBasicSet> list = this.selectByExample(example);
            return ApiResult.ok(list);
        } catch (Exception e) {
            logger.error("获取基础配置的信息失败" + e.getMessage());
        }
        return ApiResult.error("");
    }

    @Override
    public ApiResult getZhiduTree(String type) {
        try {
            JSONArray jsonArray = new JSONArray();
            Example example = new Example(TBasicSet.class);
            example.createCriteria().andEqualTo("tbType", type);
            List<TBasicSet> list = this.selectByExample(example);
            list.forEach(tBasicSet -> {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", tBasicSet.getTbId());
                jsonObject.put("title", tBasicSet.getTbName());

                Example example2 = new Example(TBasicSet.class);
                example2.createCriteria().andEqualTo("tbPid", tBasicSet.getTbId());
                List<TBasicSet> list2 = this.selectByExample(example2);
                JSONArray jsonArray1 = new JSONArray();
                list2.forEach(tBasicSet1 -> {
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("id", tBasicSet1.getTbId());
                    jsonObject1.put("title", tBasicSet1.getTbName());
                    jsonArray1.add(jsonObject1);
                });
                jsonObject.put("children", jsonArray1);
                jsonArray.add(jsonObject);
            });
            return ApiResult.ok(jsonArray);
        } catch (Exception e) {
            logger.error("解析制度树失败:" + e.getMessage());
        }
        return ApiResult.error("");
    }


}
