package com.xlcxx.web.controller.systemController;

import com.xlcxx.plodes.system.domain.Station;
import com.xlcxx.plodes.system.service.StationService;
import com.xlcxx.plodes.system.service.UserService;
import com.xlcxx.utils.ApiResult;
import com.xlcxx.web.controller.baseController.BaseController;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Description: taskmanage
 * Created by yhsh on 2019/11/6 14:23
 * version 2.0
 * 方法说明  岗位配置
 */
@RestController
@Api(tags = "岗位API")
public class StationController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(StationController.class);

    @Autowired
    private StationService stationService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "新增岗位")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tsName", value = "岗位名称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "tsPid", value = "父级岗位(顶级可为空)", dataType = "String", paramType = "query"),
    })
    @GetMapping(value = "station/addStationInfo")
    public ApiResult addStation(@ApiIgnore Station station) {
        return stationService.addStation(station);
    }

    @ApiOperation(value = "修改岗位")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tsName", value = "岗位名称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "tsId", value = "岗位id", required = true, dataType = "String", paramType = "query"),
    })
    @GetMapping(value = "station/setStationInfo")
    public ApiResult setStationInfo(@ApiIgnore Station station) {
        return stationService.updateStation(station);
    }

    @ApiOperation(value = "获取岗位树状图")
    @GetMapping(value = "station/getStationTree")
    public ApiResult getStationTree() {
        return stationService.selectAllStation();
    }

    @ApiOperation(value = "删除岗位")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tsId", value = "岗位id", required = true, dataType = "String", paramType = "query"),
    })
    @GetMapping(value = "station/delStation")
    public ApiResult delStation(@ApiIgnore Station station) {
        return stationService.deleteStation(station);
    }

    @ApiOperation(value = "新增/删除人员岗位")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户的userid", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "station", value = "岗位id(删除岗位id为空)", dataType = "String", paramType = "query"),
    })
    @GetMapping(value = "station/addUser2Station")
    public ApiResult addUser2Station(String userid, String station) {
        return userService.adjustUser2Station(userid, station);
    }

    @ApiOperation(value = "获取岗位的人员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "station", value = "岗位id", dataType = "String", paramType = "query"),
    })
    @GetMapping(value = "station/getStationUser")
    public ApiResult getStationUser(String station) {
        if (StringUtils.isAnyEmpty(station)) {
            return ApiResult.error("获取岗位参数失败");
        }
        return userService.getStationUser(station);
    }


}
