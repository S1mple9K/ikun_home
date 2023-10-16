package com.user.service.impl;

import com.ikun.pojo.Comment;
import com.ikun.return_.UserInfoReturn;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.user.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 评论业务逻辑层
 *
 * @author 9K
 * @create: 2023-07-30 15:55
 */

@Service
public class CommentServiceImpl implements CommentService {

    /**
     * 注入MongoTemplate
     */
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 查询帖子下的所有评论
     *
     * @param postId
     * @return
     */
    @Override
    public List<Comment> getPostCommentList(String postId, Long ancestorsId, Long parentId) {
        Query query = new Query(Criteria.where("post_id").is(postId).and("ancestors_id").is(ancestorsId).and("parent_id").is(parentId));
        List<Comment> list = mongoTemplate.find(query, Comment.class);
        Collections.sort(list, new Comparator<Comment>() {
            @Override
            public int compare(Comment o1, Comment o2) {
                // 先按照点赞数排序
                int likeNumComparison = Integer.compare(o2.getLikeNum(), o1.getLikeNum());
                if (likeNumComparison != 0) {
                    return likeNumComparison;
                }

                // 如果点赞数相同，则按照时间排序
                return o2.getTime().compareTo(o1.getTime());
            }
        });
        return list;
    }

    /**
     * 将该帖子的某个字段值+1
     *
     * @param parentId
     * @param key
     */
    @Override
    public void addNum(Long parentId, String key) {
        // 构造查询条件
        Query query = new Query(Criteria.where("_id").is(parentId));
        Update update = new Update().inc(key, 1);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Comment.class);
    }

    /**
     * 将该帖子的某个字段值-1
     *
     * @param parentId
     * @param key
     */
    @Override
    public void subtractNum(Long parentId, String key) {
        // 构造查询条件
        Query query = new Query(Criteria.where("_id").is(parentId));
        Update update = new Update().inc(key, -1);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Comment.class);
    }

    /**
     * 查询评论下的所有回复
     *
     * @param postId
     * @param ancestorsId
     * @return
     */
    @Override
    public List<Comment> getComReplyList(String postId, Long ancestorsId) {
        Criteria c1 = Criteria.where("ancestors_id").is(ancestorsId);
        Criteria c2 = Criteria.where("parent_id").is(ancestorsId);
        Query query = new Query(Criteria.where("post_id").is(postId).andOperator(new Criteria().orOperator(c1, c2)));
        List<Comment> list = mongoTemplate.find(query, Comment.class);
        Collections.sort(list, new Comparator<Comment>() {
            @Override
            public int compare(Comment o1, Comment o2) {
                // 先按照点赞数排序
                int likeNumComparison = Integer.compare(o2.getLikeNum(), o1.getLikeNum());
                if (likeNumComparison != 0) {
                    return likeNumComparison;
                }

                // 如果点赞数相同，则按照时间排序
                return o2.getTime().compareTo(o1.getTime());
            }
        });
        return list;
    }

    /**
     * 根据ancestorsId删除评论回复
     *
     * @param comId
     */
    @Override
    public Long deleteCommentByAncestorsId(Long comId) {
        Query query = new Query(Criteria.where("ancestors_id").is(comId));
        DeleteResult result = mongoTemplate.remove(query, Comment.class);
        long deletedCount = result.getDeletedCount();
        return deletedCount;
    }

    /**
     * 根据postId删除评论信息
     *
     * @param postId
     */
    @Override
    public void deleteCommentByPostId(String postId) {
        Query query = new Query(Criteria.where("post_id").is(postId));
        DeleteResult result = mongoTemplate.remove(query, Comment.class);
    }

    /**
     * 分页查询帖子下的所有评论
     *
     * @param postId
     * @param ancestorsId
     * @param parentId
     * @param pageNo
     * @param size
     * @return
     */
    @Override
    public Page<Comment> getPostCommentListByPage(String postId, Long ancestorsId, Long parentId, Integer pageNo, Integer size) {
        //创建查询条件
        Criteria criteria = Criteria.where("post_id").is(postId)
                .and("ancestors_id").is(ancestorsId)
                .and("parent_id").is(parentId);

        // 创建查询对象
        Query query = new Query(criteria);

        // 获取符合条件的评论总数
        long totalCount = mongoTemplate.count(query, Comment.class);

        // 创建分页请求对象
        Pageable pageable = PageRequest.of(pageNo - 1, size);

        // 设置排序条件，首先按照点赞量降序排序，然后按照最近发布时间降序排序
        query.with(Sort.by(
                Sort.Order.desc("like_count"),
                Sort.Order.desc("comment_time")
        ));

        // 设置分页
        query.with(pageable);

        // 执行查询
        List<Comment> list = mongoTemplate.find(query, Comment.class);

        // 构建分页结果对象
        return new PageImpl<>(list, pageable, totalCount);
    }

    /**
     * 评论表中的用户信息做修改
     *
     * @param userInfoReturn
     * @return
     */
    @Override
    public UpdateResult updateUserInfo(UserInfoReturn userInfoReturn) {
        return mongoTemplate.updateMulti(new Query(Criteria.where("user_id").is(userInfoReturn.getUserId())),
                new Update().set("nick_name", userInfoReturn.getNickName()).set("photo",userInfoReturn.getPhoto()), Comment.class);
    }
}
