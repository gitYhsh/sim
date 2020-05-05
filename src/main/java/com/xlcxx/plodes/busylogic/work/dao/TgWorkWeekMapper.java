package com.xlcxx.plodes.busylogic.work.dao;

import com.xlcxx.config.MyMapper;
import com.xlcxx.plodes.busylogic.work.domian.TgWorkWeek;

import java.util.List;

public interface TgWorkWeekMapper extends MyMapper<TgWorkWeek> {

	int addIndicatPublic(List<TgWorkWeek> tpp);

}