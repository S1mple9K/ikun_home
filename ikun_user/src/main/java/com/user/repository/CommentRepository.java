package com.user.repository;

import com.ikun.pojo.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * 评论表数据访问层
 * @author 9K
 * @create: 2023-07-30 15:51
 */

@Repository
public interface CommentRepository extends MongoRepository<Comment,Long> {
}
