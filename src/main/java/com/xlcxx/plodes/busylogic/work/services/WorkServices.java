package com.xlcxx.plodes.busylogic.work.services;

import com.xlcxx.plodes.baseServices.IServices;
import com.xlcxx.plodes.busylogic.work.domian.TgWorkWeek;
import com.xlcxx.utils.ApiResult;

import java.util.List;

/**
 * Description: plodes
 * Created by yhsh on 2020/4/28 9:23
 * version 2.0
 * 方法说明  工作计划接口
 */
public interface WorkServices extends IServices<TgWorkWeek> {
	/**
	 * 导入工作计划
	 * **/
	ApiResult importWorkPlan(TgWorkWeek tgWorkWeek,String path) throws Exception;

	/**删除本月计划**/
	int deleteWorkPlan(TgWorkWeek tgWorkWeek);

	/**查找提醒列表**/
	List<TgWorkWeek> selectNoticeWork(TgWorkWeek tgWorkWeek,String type);


}
