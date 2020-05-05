package com.xlcxx.plodes.busylogic.work.services.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlcxx.plodes.baseServices.RedisService;
import com.xlcxx.plodes.baseServices.impl.BaseServices;
import com.xlcxx.plodes.busylogic.work.dao.TgWorkWeekMapper;
import com.xlcxx.plodes.busylogic.work.domian.TgWorkWeek;
import com.xlcxx.plodes.busylogic.work.services.WorkServices;
import com.xlcxx.plodes.system.domain.Dept;
import com.xlcxx.plodes.system.domain.MyUser;
import com.xlcxx.plodes.system.service.DeptService;
import com.xlcxx.plodes.system.service.MenuService;
import com.xlcxx.plodes.system.service.UserService;
import com.xlcxx.utils.*;
import com.xlcxx.utils.DateUtil;
import net.bytebuddy.asm.Advice;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Description: plodes
 * Created by yhsh on 2020/4/28 9:28
 * version 2.0
 * 方法说明
 */
@Service
public  class WorkServicesImpl  extends BaseServices<TgWorkWeek> implements WorkServices {

	private static  final Logger logger = LoggerFactory.getLogger(WorkServicesImpl.class);

	@Autowired
	private UserService userService;
	@Autowired
	private TgWorkWeekMapper tgWorkWeekMapper;
	@Autowired
	private DeptService deptService;
	@Autowired
	private RedisService redisService;


	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
	public ApiResult importWorkPlan(TgWorkWeek tgWorkWeek, String path) throws Exception{
		try {
			File pathall = new File(ResourceUtils.getURL("classpath:").getPath());
			String pathExcel = path ;
			File serfile = new File(pathall.getAbsolutePath(), pathExcel);
			Workbook rwb= new XSSFWorkbook(new FileInputStream(serfile));
			Sheet sheet = rwb.getSheetAt(0);
			int rows = sheet.getLastRowNum();//获取所有得行
			if (rows <= 0){
				return ApiResult.error("导入的数据为空");
			}
			/**
			 * 获取所有的在职员工
			 * **/
			List<MyUser> users = userService.getAllMyUser();
			List<TgWorkWeek> works = new ArrayList<>();
			TgWorkWeek workWeek = null;
			for (int i = 1; i <= rows; i++) {
				Row row = sheet.getRow(i);
				/**去除空列**/
				List<Boolean> booleanList = new ArrayList<>();
				/**去除空行**/
				List<Cell> booleanList1 = new ArrayList<>();
				for (int j = 0; j < row.getPhysicalNumberOfCells() ;j++){
					Cell bpName = row.getCell(j);
					String value= " ";
					Boolean ble = POIReadExcel.isMergedRegion(sheet,i,j);
					if (ble){
						value =POIReadExcel. getMergedRegionValue(sheet,i,j);
					}else{
						value =POIReadExcel. getCellValue(bpName);
					}
					if (StringUtils.isEmpty(value)){
						booleanList1.add(bpName);
						booleanList.add(true);
					}else{ booleanList.add(false); }
				}
				if (!booleanList1.isEmpty()){
					booleanList1.forEach(cell -> row.removeCell(cell));
				}
				if (!booleanList.contains(false)){
					sheet.removeRow(row);
					break;
				}
				 int fixNum= 0;
				workWeek = new TgWorkWeek(PlodesUtils.getUUID(),"2",DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"),1);

				JSONArray jsonArray = new JSONArray();
				for (int k = row.getPhysicalNumberOfCells()-1; k>=0 ;k--){
					Cell bpName = row.getCell(k);
					Boolean ble = POIReadExcel.isMergedRegion(sheet,i,k);
					String value = ble?POIReadExcel.getMergedRegionValue(sheet,i,k):POIReadExcel.getCellValue(bpName);
					if (StringUtils.isEmpty(value)){break;}
					/**处理四个数据**/
					fixNum ++;
					if (fixNum == 1){
						List<String> listuser = Arrays.asList(value.split(","));
						List<String> listUsername = new ArrayList<>();
						for (MyUser user:users){
							if (listuser.contains(user.getNickname())){
								listUsername.add(user.getUsername());
							}
						}
						if (listUsername.isEmpty()){
							break;
						}
						workWeek.setWwDutyer(StringUtils.join(listUsername,","));
					}
					/**处理完成时间**/
					if (fixNum == 2){
						workWeek.setWwEndtime(value);
					}
					/**处理完成标准**/
					if (fixNum == 3){
						workWeek.setWwStartant(value);
					}
					/**处理工作内容**/
					if (fixNum == 4){
						workWeek.setWwContent(value);
					}
					/**处理配置文件**/
					if (fixNum > 4){
						Row rowname = sheet.getRow(0);
						String name = rowname.getCell(k).getStringCellValue();
						JSONObject jsonObject = new JSONObject(true);
						jsonObject.put("common"+k,value);
						jsonObject.put("title",name);
						jsonArray.add(jsonObject);
					}
				}
				workWeek.setWwJson(jsonArray.toJSONString());
				workWeek.setWwCreator(tgWorkWeek.getWwCreator());
				workWeek.setWwDeptUuid(tgWorkWeek.getWwDeptUuid());
				workWeek.setWwWorkTime(tgWorkWeek.getWwWorkTime());
				workWeek.setWwStatus("0");
				works.add(workWeek);
			}
			if (!works.isEmpty()){
				/**在导入之前增加删除本月计划**/
				 this.deleteWorkPlan(tgWorkWeek);
				int num = tgWorkWeekMapper.addIndicatPublic(works);
				if (num  >0 ){return ApiResult.ok("导入成功");}
			}else{
				return ApiResult.error("解析的数据为空");
			}
		}catch (Exception e){
			logger.error("导入工作计划失败："+e.getMessage());
			throw new RuntimeException("导入失败");
		}
		return ApiResult.error("导入失败");
	}

	@Override
	public List<TgWorkWeek> selectNoticeWork(TgWorkWeek tgWorkWeek, String type) {
		try {
			Example example =  new Example(TgWorkWeek.class);
			Example.Criteria criteria = example.createCriteria();

			//过期
			if ("1".equals(type)){
				List<String> status = new ArrayList<>();
				status.add("0");status.add("2");
				criteria.andCondition("(0>TIMESTAMPDIFF (DAY,DATE_FORMAT(NOW(),'%Y-%m-%d'),ww_endtime)) ").andIn("wwStatus",status);

			}else if ("2".equals(type)){
				//一天之内
				criteria.andCondition("(TIMESTAMPDIFF (DAY,DATE_FORMAT(NOW(),'%Y-%m-%d'),ww_endtime)=0)").andEqualTo("wwStatus","0");
			}else if ("3".equals(type)){
				//三天之内
				criteria.andCondition("(3>=TIMESTAMPDIFF (DAY,DATE_FORMAT(NOW(),'%Y-%m-%d'),ww_endtime)) and (TIMESTAMPDIFF (DAY,DATE_FORMAT(NOW(),'%Y-%m-%d'),ww_endtime)>0)")
						.andEqualTo("wwStatus","0");
			}else if ("4".equals(type)){
				//7天之内
				criteria.andCondition("(3>=TIMESTAMPDIFF (DAY,DATE_FORMAT(NOW(),'%Y-%m-%d'),ww_endtime)) and (TIMESTAMPDIFF (DAY,DATE_FORMAT(NOW(),'%Y-%m-%d'),ww_endtime)>0)")
						.andEqualTo("wwStatus","0");
			}
			criteria.andEqualTo("wwRlStatus","3");
			/**
			 * 查询范围
			 * 1;是不是全部
			 * 2 查询部门
			 * **/
			Map<String, String> map = redisService.hmGetAll(tgWorkWeek.getWwDutyer());
			String permis = map.get("UserPermissions");
			List<String> listPermiss = Arrays.asList(permis);
//			if (!listPermiss.contains(Permissions.ADMIN_VIEWALL_WORK)){
//				List<Dept> mydept = userService.getPersonManageDepts(tgWorkWeek.getWwDutyer());
//
//			}

			List<TgWorkWeek> list = this.selectByExample(example);

			/**处理部门名称**/
			List<Dept> depts = deptService.selectAll();
			/**处理人员名称名称**/
			List<MyUser> users = userService.selectAll();
			for (TgWorkWeek tk :list){
				/**处理人员名称名称**/
				List<String> username = new ArrayList<>();
				for (MyUser user : users){
					if (tk.getWwDutyer().contains(user.getUsername())){
						username.add(user.getNickname());break;
					}
				}
				tk.setWwDutyName(StringUtils.join(username,","));
				/**处理部门名称**/
				List<String> deptname = new ArrayList<>();
				for (Dept dept : depts){
					if (tk.getWwDeptUuid().contains(dept.getDeptId()+"")){
						deptname.add(dept.getDeptName());break;
					}
				}
				tk.setWwDeptName(StringUtils.join(deptname,","));
			}

			/**
			 * 1: 当前登陆人是不是有处理权限
			 * **/

			return list;
		}catch (Exception e){
			e.printStackTrace();

		}

		return new ArrayList<>();
	}

	@Override
	public int deleteWorkPlan(TgWorkWeek tgWorkWeek) {
		try {
			Example example = new Example(TgWorkWeek.class);
			example.createCriteria().andEqualTo("wwDeptUuid",tgWorkWeek.getWwDeptUuid());
			example.createCriteria().andEqualTo("wwWorkTime",tgWorkWeek.getWwWorkTime());
			return tgWorkWeekMapper.deleteByExample(example);

		}catch (Exception e){
			logger.error("删除本月部门计划失败："+e.getMessage());
		}
		return 0;
	}






}
