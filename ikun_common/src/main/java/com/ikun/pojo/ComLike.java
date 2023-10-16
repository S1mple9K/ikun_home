package com.ikun.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 评论点赞实体类
 * @author 9K
 * @create: 2023-07-31 19:43
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "comLike")
public class ComLike {
    /**
     * 点赞id
     */
    @Id
    private String id;

    /**
     * 帖子id
     */
    @Field("post_id")
    private String postId;

    /**
     * 评论id
     */
    @Field("com_id")
    private Long comId;

    /**
     * 点赞用户id
     */
    @Field("user_id")
    private String userId;
}
