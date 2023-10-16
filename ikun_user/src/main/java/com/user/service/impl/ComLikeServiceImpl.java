package com.user.service.impl;

import com.ikun.pojo.ComLike;
import com.user.service.ComLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 评论点赞业务逻辑层
 *
 * @author 9K
 * @create: 2023-07-31 19:57
 */

@Service
public class ComLikeServiceImpl implements ComLikeService {

    /**
     * 注入MongoTemplate
     */
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 从点赞表中删除点赞数据
     *
     * @param comId
     * @param userId
     */
    @Override
    public void cancelLike(Long comId, String userId) {
        Query query = new Query(Criteria.where("com_id").is(comId).and("user_id").is(userId));
        mongoTemplate.remove(query, ComLike.class);
    }

    /**
     * 根据用户id遍历点赞表,查看用户是否点赞了该帖子
     *
     * @param userId
     * @return
     */
    @Override
    public List<ComLike> showUserLikePost(String userId) {
        Query query = new Query(Criteria.where("user_id").is(userId));
        return mongoTemplate.find(query, ComLike.class);
    }

    /**
     * 根据评论id删除评论点赞信息
     * @param comId
     */
    @Override
    public void deleteComLikeByComId(Long comId) {
        Query query = new Query(Criteria.where("com_id").is(comId));
        mongoTemplate.remove(query, ComLike.class);
    }

    /**
     * 根据帖子id删除评论点赞信息
     * @param postId
     */
    @Override
    public void deleteComLikeByPostId(String postId) {
        Query query = new Query(Criteria.where("post_id").is(postId));
        mongoTemplate.remove(query, ComLike.class);
    }
}
