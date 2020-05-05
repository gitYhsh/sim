package com.xlcxx.web.controller.systemController;


import com.xlcxx.plodes.system.service.TbBasicServices;
import com.xlcxx.utils.ApiResult;
import com.xlcxx.web.controller.baseController.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Description: taskmanage
 * Created by yhsh on 2019/11/18 10:23
 * version 2.0
 * 方法说明  基础设置路由
 */
@Api(tags = "基础设置")
@RestController
public class BasicSetController extends BaseController {

    @Autowired
    private TbBasicServices tbBasicServices;

    @GetMapping(value = "basic/getBasicTypeBytype")
    public ApiResult getBasicTypeBytype(String type) {
        return tbBasicServices.getBasicConfig(type);
    }

    @PostMapping(value = "basic/getBasicConfigSub")
    public ApiResult getBasicConfigSub(String pid) {
        return tbBasicServices.getBasicConfigSub(pid);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型 公司通式 1 部门交接规范", required = true, dataType = "String", paramType = "query"),
    })
    @GetMapping(value = "basic/getBasicConfigSubTree")
    public ApiResult getBasicConfigSubTree(String type) {
        return tbBasicServices.getZhiduTree(type);
    }


}
