package com.ikun.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 发布帖子实体类
 *
 * @author 9K
 * @create: 2023-07-26 17:21
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostData {
    private String userId;
    private String nickName;
    private String photo;
    private String content;
    private Long tagId;
}
