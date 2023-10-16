package com.ikun.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 发布评论数据封装实体类
 * @author 9K
 * @create: 2023-07-30 17:19
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentData {
    /**
     * 评论帖/回复的人的id
     */
    private String postId;
    /**
     * 回复的人的昵称
     */
    private String comName;
    private String userId;
    private String nickName;
    private String photo;
    /**
     * 祖级id
     */
    private Long ancestorsId;
    private Long parentId;
    private String content;
}
