package com.user.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;

/**
 * 阿里云短信服务
 * @author 9K
 * @create: 2023-06-15 20:55
 */

public class AliyunSmsSender {
    /**
     * 阿里云 Access Key ID
     */
    private static final String ACCESS_KEY_ID = "";

    /**
     * 阿里云 Access Key Secret
     */
    private static final String ACCESS_KEY_SECRET = "";

    /**
     * 短信签名
     */
    private static final String SIGN_NAME = "";

    /**
     * 短信模板代码
     */
    private static final String TEMPLATE_CODE = "";

    /**
     * 短信发送方法
     * @param phone 手机号
     * @param code 验证码
     */
    public static String sendSms(String phone,String code){
        // 创建 DefaultAcsClient 实例并初始化
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        IAcsClient client = new DefaultAcsClient(profile);

        // 创建 SendSmsRequest 实例并设置参数
        SendSmsRequest request = new SendSmsRequest();
        request.setSysRegionId("cn-hangzhou");
        //设置短信签名
        request.setSignName(SIGN_NAME);
        //设置短信模板代码
        request.setTemplateCode(TEMPLATE_CODE);
        //设置接收短信的手机号
        request.setPhoneNumbers(phone);
        //设置验证码
        request.setTemplateParam("{\"code\":\""+code+"\"}");
        // 发送短信并处理响应
        try {
            SendSmsResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
            return "ok";
        } catch (ServerException e) {
        } catch (ClientException e) {
        }
        return "error";
    }
}
