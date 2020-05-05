package com.xlcxx.web.controller.systemController;


import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.xlcxx.plodes.system.service.MenuService;
import com.xlcxx.utils.ApiResult;
import com.xlcxx.utils.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(tags = "公共基础接口")
@RestController
public class CommonController {

    @Autowired
    private MenuService menuService;

    @ApiOperation(value = "文件上传(可多选)")
    @PostMapping(value = "file/upload", headers = "content-type=multipart/form-data")
    public ApiResult fileUploadController(@RequestParam(name = "file") MultipartFile[] file) {
        System.out.println(file.length);
        if (file.length <= 0) {
            return ApiResult.error("文件就收失败");
        }
        return FileUtil.excuteFileUpload(file);
    }


    @ApiOperation(value = "根据菜单获取此菜单页面所有的权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId", value = "当前菜单id(如果为空则获取所有页面权限)", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "username", value = "username", required = true, dataType = "string", paramType = "query"),
    })
    @GetMapping(value = "userbtn/UserPermissions")
    public ApiResult getUserPermissions(String menuId, String username) {
        if (StringUtils.isEmpty(username)) {
            return ApiResult.error("获取参数失败");
        }
        List<String> permissions = menuService.findMenuButtonByUseridAndMenuId(menuId, username);
        return ApiResult.ok(permissions);
    }


}
