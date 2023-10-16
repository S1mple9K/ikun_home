package com.user.service.impl;

import com.ikun.pojo.Like;
import com.user.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 点赞表业务逻辑层
 *
 * @author 9K
 * @create: 2023-07-27 14:18
 */

@Service
public class LikeServiceImpl implements LikeService {

    /**
     * 注入MongoTemplate
     */
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 根据帖子id和用户id取消点赞信息
     *
     * @param postId
     * @param userId
     */
    @Override
    public void cancelLike(String postId, String userId) {
        Query query = new Query(Criteria.where("post_id").is(postId).and("user_id").is(userId));
        mongoTemplate.remove(query, Like.class);
    }

    /**
     * 查询用户点赞的帖子
     *
     * @param userId
     * @return
     */
    @Override
    public List<Like> showUserLikePost(String userId) {
        Query query = new Query(Criteria.where("user_id").is(userId));
        return mongoTemplate.find(query, Like.class);
    }

    /**
     * 根据用户id和帖子id查询点赞表,查看用户是否点赞了该帖子
     *
     * @param userId
     * @return
     */
    @Override
    public Integer showUserLikePostByUserIdAndPostId(String userId, String postId) {
        // 根据用户id和帖子id查询点赞表
        Query query = new Query(Criteria.where("user_id").is(userId).and("post_id").is(postId));
        Like like = mongoTemplate.findOne(query, Like.class);
        return like != null ? 1 : 0;
    }

    /**
     * 根据postId删除点赞
     * @param postId
     */
    @Override
    public void deleteLikeByPostId(String postId) {
        Query query = new Query(Criteria.where("post_id").is(postId));
        mongoTemplate.remove(query,Like.class);
    }
}
