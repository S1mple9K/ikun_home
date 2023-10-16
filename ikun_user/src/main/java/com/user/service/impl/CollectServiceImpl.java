package com.user.service.impl;

import com.ikun.pojo.Collect;
import com.user.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收藏表业务逻辑层
 * @author 9K
 * @create: 2023-07-27 15:13
 */

@Service
public class CollectServiceImpl implements CollectService {

    /**
     * 注入MongoTemplate
     */
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 删除收藏信息
     * @param postId
     * @param userId
     */
    @Override
    public void cancelCollect(String postId, String userId) {
        Query query = new Query(Criteria.where("post_id").is(postId).and("user_id").is(userId));
        mongoTemplate.remove(query, Collect.class);
    }

    /**
     * 查询用户收藏的帖子
     * @param userId
     * @return
     */
    @Override
    public List<Collect> showUserCollectPost(String userId) {
        Query query = new Query(Criteria.where("user_id").is(userId));
        return mongoTemplate.find(query, Collect.class);
    }

    /**
     * 根据用户id和帖子id查询收藏表,查看用户是否收藏了该帖子
     * @param userId
     * @param postId
     * @return
     */
    @Override
    public Integer showUserCollectPostByUserIdAndPostId(String userId, String postId) {
        Query query = new Query(Criteria.where("user_id").is(userId).and("post_id").is(postId));
        Collect collect = mongoTemplate.findOne(query, Collect.class);
        return collect != null ? 1 : 0;
    }
}
