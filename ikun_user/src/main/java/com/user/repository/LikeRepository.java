package com.user.repository;

import com.ikun.pojo.Like;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * 点赞表数据访问层
 * @author 9K
 * @create: 2023-07-27 14:11
 */

@Repository
public interface LikeRepository extends MongoRepository<Like,String> {
}
