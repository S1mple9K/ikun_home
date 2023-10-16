package com.user.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.controller.PostController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * 获取ip工具类
 * @author 9K
 * @create: 2023-08-03 14:19
 */

public class IpUtil {

    /**
     * 用户登录获取位置
     */
    public static String getIpLocation() {
        String ip = getExternalIP();
        String location = getLocationByIP(ip);
        //标记为已查询过ip
        PostController.queryIpFlag=true;
        return location;
    }

    /**
     * 获取用户外部ip
     */
    public static String getExternalIP() {
        JsonNode jsonNode = null;
        try {
            URL url = new URL("https://api.ipify.org?format=json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String response = reader.readLine();
            reader.close();

            ObjectMapper objectMapper = new ObjectMapper();
            jsonNode = objectMapper.readTree(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return jsonNode.get("ip").asText();
    }

    /**
     * 获取用户省份
     */
    public static String getLocationByIP(String ip) {
        JsonNode jsonNode = null;
        try {
            String url = "https://ipapi.co/" + ip + "/json/";
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            ObjectMapper objectMapper = new ObjectMapper();
            jsonNode = objectMapper.readTree(response.toString());
            System.out.println("jsonNode = " + jsonNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return jsonNode.get("city").asText();
    }
}
