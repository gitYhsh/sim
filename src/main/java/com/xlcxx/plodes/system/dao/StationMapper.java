package com.xlcxx.plodes.system.dao;

import com.xlcxx.config.MyMapper;
import com.xlcxx.plodes.system.domain.Station;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StationMapper extends MyMapper<Station> {
}