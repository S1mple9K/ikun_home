package com.user.utils;

import com.alibaba.fastjson.JSON;
import com.ikun.chat.ResultMessage;

/**
 * 封装json格式消息工具类
 * @author 9K
 * @create: 2023-10-11 23:16
 */

public class MessageUtils {
    public static String getMessage(boolean isSystemMessage,String fromName,Object message){
        ResultMessage result = new ResultMessage();
        result.setSystem(isSystemMessage);
        result.setMessage(message);
        if(fromName!=null){
            result.setFromName(fromName);
        }
        return JSON.toJSONString(result);
    }
}
