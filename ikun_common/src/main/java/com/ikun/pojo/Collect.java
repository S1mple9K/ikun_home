package com.ikun.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 帖子收藏表
 * @author 9K
 * @create: 2023-07-27 14:05
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "collect")
public class Collect {
    /**
     * 收藏id
     */
    @Id
    private String id;

    /**
     * 帖子id
     */
    @Field("post_id")
    private String postId;

    /**
     * 收藏用户id
     */
    @Field("user_id")
    private String userId;
}
