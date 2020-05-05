package com.xlcxx.web.controller.workController.workplan;

import com.xlcxx.config.aspect.ValifyToken;
import com.xlcxx.plodes.busylogic.work.domian.TgWorkWeek;
import com.xlcxx.plodes.busylogic.work.services.WorkServices;
import com.xlcxx.utils.ApiResult;
import com.xlcxx.web.controller.baseController.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * Description: plodes
 * Created by yhsh on 2020/4/28 9:44
 * version 2.0
 * 方法说明
 */
@RestController
@Api(tags = "工作管理")
public class WorkPlanController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(WorkPlanController.class);

	@Autowired
	private WorkServices workServices;


	@ApiOperation(value = "工作计划导入")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "wwDeptUuid", value = "部门id 所选部门", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "wwWorkTime", value = "工作时间", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "wwCreator", value = "创建人", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "path", value = "导入路径", required = true, dataType = "String", paramType = "query"),
	})
	@PostMapping(value = "work/import")
	public ApiResult workImport(@ApiIgnore TgWorkWeek tgWorkWeek, @ApiIgnore String path){
		try {
			return workServices.importWorkPlan(tgWorkWeek,path);
		}catch (Exception e){
			return ApiResult.error(e.getMessage());
		}
	}

	@ApiOperation(value = "工作计划提醒列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "type", value = "类型(1过期 2一天 3三天4 七天)", required = true, dataType = "String", paramType = "query"),
	})
	@PostMapping(value = "work/selectNotice")
	public ApiResult selectNotice(@ApiIgnore TgWorkWeek tgWorkWeek, @ApiIgnore String type){
			List<TgWorkWeek> list =  workServices.selectNoticeWork(tgWorkWeek,type);
			return ApiResult.ok(list);
	}



}
