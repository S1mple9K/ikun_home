package com.ikun.chat;

import lombok.Data;

/**
 * @version v1.0
 * @ClassName: ResultMessage
 * @Description: 用来封装服务端给浏览器发送的消息数据
 * @Author: 黑马程序员
 */
@Data
public class ResultMessage {

    private boolean isSystem;
    private String fromName;
    //如果是系统消息是数组
    private Object message;
}
