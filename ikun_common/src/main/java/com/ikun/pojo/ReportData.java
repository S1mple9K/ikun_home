package com.ikun.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 举报帖子对应实体类
 * @author 9K
 * @create: 2023-07-29 14:39
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "reportData")
public class ReportData {
    /**
     * 主键id
     */
    @Id
    private Long id;

    /**
     * 帖子id
     */
    @Field("post_id")
    private String postId;

    /**
     * 发帖人id
     */
    @Field("post_user_id")
    private String postUserId;

    /**
     * 发帖人昵称
     */
    @Field("post_user_name")
    private String postUserNickName;

    /**
     * 举报者id
     */
    @Field("user_id")
    private String userId;

    /**
     * 举报者昵称
     */
    @Field("nick_name")
    private String nickName;

    /**
     * 举报者头像
     */
    @Field("photo")
    private String photo;

    /**
     * 举报标签
     */
    @Field("report_tag")
    private String reportTag;

    /**
     * 举报内容
     */
    @Field("content")
    private String content;

    /**
     * 举报时间
     */
    @Field("date")
    private String date;

    /**
     * 是否查看
     */
    @Field("is_view")
    private Integer isView;

    /**
     * 是否处理
     */
    @Field("is_handle")
    private Integer isHandle;

}
