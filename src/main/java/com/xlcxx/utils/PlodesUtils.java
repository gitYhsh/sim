package com.xlcxx.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlodesUtils {
    /**
     * 获取UUID 做为数据库各个表的主键,不采用自增长
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }

    /**
     * 简单判断是否为手机号
     *
     * @param phoneNo 手机号
     * @return boolean
     */
    public static boolean isPhoneNo(String phoneNo) {
        String regex = "[1]\\d{10}";
        if (StringUtils.isBlank(phoneNo))
            return false;
        else
            return phoneNo.matches(regex);
    }

    public static BitMatrix generateQRCodeStream(String content, HttpServletResponse response) {
        //给相应添加头部信息，主要告诉浏览器返回的是图片流
        response.setHeader("Cache-Control", "no-store");
        // 不设置缓存
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/png");
        //设置图片的文字编码以及内边框
        Map<EncodeHintType, Object> hints = new HashMap<>();
        //编码
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        //边框距
        hints.put(EncodeHintType.MARGIN, 0);
        BitMatrix bitMatrix;
        try {
            //参数分别为：编码内容、编码类型、图片宽度、图片高度，设置参数
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 300, 300, hints);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
        return bitMatrix;
    }


    /**
     * 表情包 和一些特殊字符 处理
     **/
    public static String filterEmoji(String source) {
        int len = source.length();
        StringBuilder buf = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isNotEmojiCharacter(codePoint)) {
                buf.append(codePoint);
            } else {
                buf.append("*");
            }
        }
        return buf.toString();
    }

    /**
     * 處理表情包
     **/
    private static boolean isNotEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) ||
                (codePoint == 0xA) || (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * 获取字符串中的数据
     */
    public static Integer getNumber(String number) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(number);
        return Integer.valueOf(m.replaceAll("").trim());
    }

}
