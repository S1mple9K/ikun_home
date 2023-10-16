package com.ikun.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 关注表实体类
 * @author 9K
 * @create: 2023-07-27 23:29
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("care")
public class Care {
    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Integer id;

    /**
     * 关注者id(当前用户)
     */
    private String userId;

    /**
     * 关注者昵称(当前用户)
     */
    private String userName;

    /**
     * 被关注者id(发帖用户)
     */
    private String postUserId;

    /**
     * 被关注者id(发帖用户)
     */
    private String postUserName;

    /**
     * 发帖者头像
     */
    private String photo;

}
