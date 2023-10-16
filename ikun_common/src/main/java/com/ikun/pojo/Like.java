package com.ikun.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 帖子点赞表
 * @author 9K
 * @create: 2023-07-27 14:05
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "like")
public class Like {
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
     * 点赞用户id
     */
    @Field("user_id")
    private String userId;
}
