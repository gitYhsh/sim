package com.xlcxx.utils;

import com.alibaba.fastjson.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


/**
 * 创建时间：2018年6月1日 上午8:58:42
 * 项目名称：taskmanage
 *
 * @author yhsh
 * @version 1.0
 */
public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 多文件上传
     **/
    public static ApiResult excuteFileUpload(MultipartFile[] file) {
        if (file.length <= 0) {
            return ApiResult.error(" file is null or url error");
        }
        try {
            File pathall = new File(ResourceUtils.getURL("classpath:").getPath());
            JSONArray array = new JSONArray();
            for (int i = 0; i < file.length; i++) {
                String SourceFileName = file[i].getOriginalFilename();
                String filename = SourceFileName.substring(SourceFileName.lastIndexOf("."));
                String path = "static/temp/" + System.currentTimeMillis() + filename;
                File serfile = new File(pathall.getAbsolutePath(), path);
                file[i].transferTo(serfile);
                array.add(path);
            }
            return ApiResult.ok(array);
        } catch (IOException e) {
            logger.error("文件路径找不到：" + e.getMessage());
        }
        return ApiResult.error("");
    }

    /**
     * 文件解析成Excl
     **/
    public static String paraseOfficeToExcl(String url) {
        try {
            String path = ResourceUtils.getURL("classpath:").getPath();
            JSONArray content = POIReadExcel.excelWriteToHtml(path + url);
            return content.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
