package com.ikun.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 后台管理系统获取用户列表
 * @author 9K
 * @create: 2023-07-21 13:48
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryUser {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 注册时间
     */
    private String createTime;

    /**
     * 状态值
     */
    private Integer status;

    /**
     * 是否禁言
     */
    private Integer isProhibit;

    /**
     * 禁言结束时间
     */
    private String endTime;
}
