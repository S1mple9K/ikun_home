package com.user.repository;

import com.ikun.pojo.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 帖子数据访问层
 * @author 9K
 * @create: 2023-07-21 18:56
 */

@Repository
public interface PostRepository extends MongoRepository<Post,String> {

    /**
     * 获取我发布的帖子列表
     * @param userId
     * @return
     */
    List<Post> getPostsByUserId(String userId);

    /**
     * 根据帖子id查找帖子
     */
    Post getPostById(String postId);
}
