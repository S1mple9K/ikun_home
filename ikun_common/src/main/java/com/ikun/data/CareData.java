package com.ikun.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户关注/取关操作
 * @author 9K
 * @create: 2023-07-28 13:35
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareData {
    /**
     * 发帖人(即被关注者id)
     */
    private String postUserId;

    /**
     * 发帖人昵称
     */
    private String postNickName;

    /**
     * 发帖人头像
     */
    private String photo;

    /**
     * 当前用户id(关注者)
     */
    private String userId;

    /**
     * 当前用户昵称
     */
    private String userNickName;

    /**
     * 区分关注还是取消
     */
    private Integer isCare;
}
