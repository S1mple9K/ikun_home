package com.ikun.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 评论表实体类
 *
 * @author 9K
 * @create: 2023-07-30 15:47
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "comment")
public class Comment {
    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 评论帖/回复的人的id
     */
    @Field("post_id")
    private String postId;

    /**
     * 回复的人的昵称
     */
    @Field("com_name")
    private String comName;

    /**
     * 评论人id
     */
    @Field("user_id")
    private String userId;

    /**
     * 评论人昵称
     */
    @Field("nick_name")
    private String nickName;

    /**
     * 评论人头像
     */
    @Field("photo")
    private String photo;

    /**
     * 评论内容
     */
    @Field("content")
    private String content;

    /**
     * 评论人时间
     */
    @Field("time")
    private String time;

    /**
     * 祖级id
     */
    @Field("ancestors_id")
    private Long ancestorsId;

    /**
     * 父级id
     */
    @Field("parent_id")
    private Long parentId;

    /**
     * 点赞数
     */
    @Field("like_num")
    private Integer likeNum;

    /**
     * 回复数
     */
    @Field("reply_num")
    private Integer replyNum;
}
