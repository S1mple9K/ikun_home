package com.ikun.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户实体类
 *
 * @author 9K
 * @create: 2023-07-21 13:04
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
public class User {

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

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
    private Integer sex;

    /**
     * 用户年龄
     */
    private Integer age;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 个性签名
     */
    private String sign;

    /**
     * 用户头像
     */
    private String photo;

    /**
     * 注册时间
     */
    private String createTime;

    /**
     * 状态值,1:可用,0:禁用
     */
    private Integer status;

    /**
     * 是否禁言,1:禁言,0:正常
     */
    private Integer isProhibit;

    /**
     * 禁言结束时间
     */
    private String endTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer isDelete;
}
