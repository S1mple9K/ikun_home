package com.ikun.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 标签分类表
 * @author 9K
 * @create: 2023-07-21 18:57
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tag")
public class Tag {
    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 标签名称
     */
    @Field("tag_name")
    private String tagName;

    /**
     * 标签简介
     */
    @Field("tag_content")
    private String tagContent;

    /**
     * 父标签
     */
    @Field("parent_id")
    private Long parentId;

    /**
     * 状态:是否禁用
     */
    private Integer status;
}
