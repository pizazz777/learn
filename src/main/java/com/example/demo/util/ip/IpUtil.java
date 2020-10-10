package com.example.demo.util.ip;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author administrator
 * @date 2020/05/15
 * @description 类说明：IP工具类
 */
@Slf4j
public class IpUtil {

    private IpUtil() {
    }

    /**
     * 获取 ip 所在区域
     *
     * @param ip ip 地址
     * @return 区域json 信息
     */
    public static JSONObject getIpArea(String ip) {
        String path = "http://ip.taobao.com/outGetIpInfo?accessKey=alibaba-inc&ip=" + ip;
        String inputLine;
        StringBuilder info = new StringBuilder();
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10 * 1000);
            conn.setRequestMethod("GET");
            try (InputStreamReader reader = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
                 BufferedReader buffer = new BufferedReader(reader)) {
                while ((inputLine = buffer.readLine()) != null) {
                    info.append(inputLine);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = null;
        if (StringUtils.isNotBlank(info)) {
            String infoStr = info.toString();
            if ("0".equals(String.valueOf(JSON.parseObject(infoStr).get("code")))) {
                jsonObject = (JSONObject) JSON.parseObject(infoStr).get("data");
            }
        }
        return jsonObject;
    }


    /**
     * 根据 httpServletRequest 获取真实 ip
     *
     * @param request httpServletRequest
     * @return 请求 ip
     */
    public static String getIp(HttpServletRequest request) {
        String unknown = "unknown";
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            // String localhostIp = "127.0.0.1";
            // String localhostIp2 = "0:0:0:0:0:0:0:1";
            // if (localhostIp.equals(ip) || localhostIp2.equals(ip)) {
            //     /**
            //      * 根据网卡取本机配置的IP
            //      */
            //     InetAddress inetAddress = null;
            //     try {
            //         inetAddress = InetAddress.getLocalHost();
            //         ip = inetAddress.getHostAddress();
            //     } catch (Exception e) {
            //         e.printStackTrace();
            //     }
            // }
        }
        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null) {
            int index = ip.indexOf(",");
            if (index > 0) {
                ip = ip.substring(0, index);
            }
        }
        return ip;
    }
}
