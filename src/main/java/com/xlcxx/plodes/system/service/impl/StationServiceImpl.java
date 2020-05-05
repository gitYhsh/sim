package com.xlcxx.plodes.system.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlcxx.plodes.baseServices.impl.BaseServices;
import com.xlcxx.plodes.system.dao.StationMapper;
import com.xlcxx.plodes.system.domain.Station;
import com.xlcxx.plodes.system.service.StationService;
import com.xlcxx.utils.ApiResult;
import com.xlcxx.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Description: taskmanage
 * Created by yhsh on 2019/11/6 14:25
 * version 2.0
 * 方法说明
 */
@Service
public class StationServiceImpl extends BaseServices<Station> implements StationService {

    private final static Logger logger = LoggerFactory.getLogger(StationServiceImpl.class);
    @Autowired
    private StationMapper stationMapper;

    @Override
    public ApiResult addStation(Station station) {
        try {
            station.setTsStatus("1");
            station.setTsTime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
            int num = stationMapper.insert(station);
            return num > 0 ? ApiResult.ok(station) : ApiResult.error("新增岗位失败");
        } catch (Exception e) {
            logger.error("新增岗位失败:" + e.getMessage());
        }
        return ApiResult.error("新增岗位失败");
    }

    @Override
    public ApiResult updateStation(Station station) {
        try {
            int num = stationMapper.updateByPrimaryKeySelective(station);
            return num > 0 ? ApiResult.ok("修改成功") : ApiResult.error("修改失败");
        } catch (Exception e) {
            logger.error("修改岗位失败：" + e.getMessage());
        }
        return ApiResult.error("修改失败");
    }

    @Override
    public ApiResult selectAllStation() {
        try {
            Example example = new Example(Station.class);
            example.createCriteria().andIsNull("tsPid").andEqualTo("tsStatus", "1");
            List<Station> list = this.selectByExample(example);
            JSONArray array = paraseStation(list);
            return ApiResult.ok(array);
        } catch (Exception e) {
            logger.error("获取岗位树状图失败: " + e.getMessage());
        }
        return ApiResult.error("");
    }

    private JSONArray paraseStation(List<Station> stations) {
        JSONArray jsonArray = new JSONArray();
        for (Station station : stations) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("tsId", station.getTsId());
            jsonObject.put("name", station.getTsName());
            Example example = new Example(Station.class);
            example.createCriteria().andEqualTo("tsPid", station.getTsId()).andEqualTo("tsStatus", "1");
            List<Station> list = this.selectByExample(example);
            jsonObject.put("childs", paraseStation(list));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    @Override
    public ApiResult deleteStation(Station station) {
        try {
            station.setTsStatus("0");
            int num = this.updateNotNull(station);
            return num > 0 ? ApiResult.ok("") : ApiResult.error("删除失败");
        } catch (Exception e) {
            logger.error("删除岗位失败: " + e.getMessage());
        }
        return ApiResult.error("删除失败");
    }
}
