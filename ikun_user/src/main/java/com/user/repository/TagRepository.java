package com.user.repository;

import com.ikun.pojo.Tag;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 标签分类数据访问层
 * @author 9K
 * @create: 2023-07-21 18:56
 */

@Repository
public interface TagRepository extends MongoRepository<Tag,Long> {

    /**
     * 根据parentId查询分类
     * @param parentId 父id
     * @return
     */
    List<Tag> getTagsByParentId(Long parentId);
}
