package com.ikun.return_;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户查询基本信息返回实体类
 * @author 9K
 * @create: 2023-07-25 14:24
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoReturn {
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户性别
     */
    private String sex;

    /**
     * 用户年龄
     */
    private Integer age;

    /**
     * 用户签名
     */
    private String sign;

    /**
     * 用户注册时间
     */
    private String createTime;

    /**
     * 用户头像
     */
    private String photo;

    /**
     * 用户位置
     */
    private String ip;
}
