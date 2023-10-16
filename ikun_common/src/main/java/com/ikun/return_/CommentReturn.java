package com.ikun.return_;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评论表实体类
 *
 * @author 9K
 * @create: 2023-07-30 15:47
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentReturn {
    /**
     * 主键
     */
    private Long id;

    /**
     * 评论帖/回复的人的id
     */
    private String postId;

    /**
     * 回复的人的昵称
     */
    private String comName;

    /**
     * 评论人id
     */
    private String userId;

    /**
     * 评论人昵称
     */
    private String nickName;

    /**
     * 评论人头像
     */
    private String photo;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论人时间
     */
    private String time;

    /**
     * 祖级id
     */
    private Long ancestorsId;

    /**
     * 父级id
     */
    private Long parentId;

    /**
     * 点赞数
     */
    private Integer likeNum;

    /**
     * 回复数
     */
    private Integer replyNum;

    /**
     * 是否点赞
     */
    private Integer isLike;
}
