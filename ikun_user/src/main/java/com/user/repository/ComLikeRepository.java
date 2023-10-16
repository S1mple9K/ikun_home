package com.user.repository;

import com.ikun.pojo.ComLike;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * 评论点赞表数据访问层
 * @author 9K
 * @create: 2023-07-31 19:44
 */

@Repository
public interface ComLikeRepository extends MongoRepository<ComLike,String> {
}
