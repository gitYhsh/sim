package com.xlcxx.plodes.system.service;

import com.xlcxx.plodes.baseServices.IServices;
import com.xlcxx.plodes.system.domain.Station;
import com.xlcxx.utils.ApiResult;

/**
 * Description: taskmanage
 * Created by yhsh on 2019/11/6 14:25
 * version 2.0
 * 方法说明
 */
public interface StationService extends IServices<Station> {

    /**
     * 增加岗位
     **/
    ApiResult addStation(Station station);

    /**
     * 修改岗位名称
     **/
    ApiResult updateStation(Station station);

    /**
     * 查询树状岗位
     **/
    ApiResult selectAllStation();

    /**
     * 删除岗位
     **/
    ApiResult deleteStation(Station station);

}
