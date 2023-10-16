package com.ikun.return_;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 帖子返回类
 * @author 9K
 * @create: 2023-07-26 13:54
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostReturn {

    /**
     * 主键id
     */
    @Id
    private String id;

    /**
     * 发布人id
     */
    @Field("user_id")
    private String userId;

    /**
     * 发布人昵称
     */
    @Field("nick_name")
    private String nickName;

    /**
     * 用户头像
     */
    @Field("photo")
    private String photo;

    /**
     * 帖子内容
     */
    private String content;

    /**
     * 发布图片
     */
    private String[] images;

    /**
     * 浏览量
     */
    @Field("view_num")
    private Integer viewNum;

    /**
     * 点赞量
     */
    @Field("like_num")
    private Integer likeNum;

    /**
     * 评论数
     */
    @Field("comment_num")
    private Integer commentNum;

    /**
     * 收藏数
     */
    @Field("collect_num")
    private Integer collectNum;

    /**
     * 标签分类
     */
    @Field("tag_id")
    private Long tagId;

    /**
     * 发布时间
     */
    @Field("release_time")
    private String releaseTime;

    /**
     * 当前权重
     */
    @Field("weight")
    private float weight;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 当前用户是否点赞该帖子
     */
    private Integer isLike;

    /**
     * 当前用户是否收藏该帖子
     */
    private Integer isCollect;

    /**
     * 当前用户是否关注该帖主
     */
    private Integer isCare;
}
