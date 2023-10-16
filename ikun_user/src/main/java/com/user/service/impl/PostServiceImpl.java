package com.user.service.impl;

import com.ikun.pojo.Post;
import com.mongodb.client.result.UpdateResult;
import com.user.repository.PostRepository;
import com.user.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 帖子业务逻辑层
 *
 * @author 9K
 * @create: 2023-07-26 14:04
 */
@Service
public class PostServiceImpl implements PostService {

    /**
     * 注入PostRepository
     */
    @Autowired
    private PostRepository postRepository;

    /**
     * 注入MongodbTemplate
     */
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 修改用户昵称
     *
     * @param userId
     * @param nickName
     */
    @Override
    public UpdateResult updateUserNickName(String userId, String nickName) {
        return mongoTemplate.updateMulti(new Query(Criteria.where("user_id").is(userId)),
                new Update().set("nick_name", nickName), Post.class);
    }

    /**
     * 修改用户头像
     *
     * @param userId
     * @param url
     */
    @Override
    public void updateUserPhoto(String userId, String url) {
        mongoTemplate.updateMulti(new Query(Criteria.where("user_id").is(userId)),
                new Update().set("photo", url), Post.class);
    }

    /**
     * 获取帖子列表
     *
     * @return
     */
    @Override
    public List<Post> getPostList() {
        List<Post> list = postRepository.findAll();
        //排序
        Collections.sort(list, new Comparator<Post>() {
            @Override
            public int compare(Post o1, Post o2) {
                // 按照权重值进行排序
                if (o1.getWeight() > o2.getWeight()) {
                    return -1; // o1的权重值大于o2的权重值，o1排在o2前面
                } else if (o1.getWeight() < o2.getWeight()) {
                    return 1; // o1的权重值小于o2的权重值，o1排在o2后面
                } else {
                    // 权重值相同，按照日期排序
                    return o2.getReleaseTime().compareTo(o1.getReleaseTime());
                }
            }
        });
        return list;
    }

    /**
     * 获取我发布的帖子列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<Post> getMyPostList(String userId) {
        List<Post> list = postRepository.getPostsByUserId(userId);
        //排序
        Collections.sort(list, new Comparator<Post>() {
            @Override
            public int compare(Post o1, Post o2) {
                // 按照权重值进行排序
                if (o1.getWeight() > o2.getWeight()) {
                    return -1; // o1的权重值大于o2的权重值，o1排在o2前面
                } else if (o1.getWeight() < o2.getWeight()) {
                    return 1; // o1的权重值小于o2的权重值，o1排在o2后面
                } else {
                    // 权重值相同，按照日期排序
                    return o2.getReleaseTime().compareTo(o1.getReleaseTime());
                }
            }
        });
        return list;
    }

    /**
     * 将该帖子的某个字段值+1
     *
     * @param postId
     * @param key    字段
     */
    @Override
    public void addNum(String postId, String key) {
        // 构造查询条件
        Query query = new Query(Criteria.where("_id").is(postId));
        Update update = new Update().inc(key, 1);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Post.class);
    }

    /**
     * 将帖子的某个字段-num
     *
     * @param postId
     * @param key
     */
    @Override
    public void subtractNum(String postId, String key, Integer num) {
        // 构造查询条件
        Query query = new Query(Criteria.where("_id").is(postId));
        Update update = new Update().inc(key, -num);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Post.class);
    }

    /**
     * 获取关注用户帖子
     *
     * @param postUserId
     * @return
     */
    @Override
    public List<Post> getCareList(String postUserId) {
        // 构造查询条件
        Query query = new Query(Criteria.where("user_id").is(postUserId));
        List<Post> postList = mongoTemplate.find(query, Post.class);
        return postList;
    }

    /**
     * 分页查询所有帖子
     *
     * @param pageNo
     * @param size
     * @return
     */
    @Override
    public List<Post> getAllPostListByPage(Integer pageNo, Integer size) {
        Pageable pageable = PageRequest.of(pageNo, size);
        Page<Post> posts = postRepository.findAll(pageable);
        List<Post> list = posts.getContent();
        return list;
    }

    /**
     * 分页查询我发布的帖子
     *
     * @param userId
     * @param pageNo
     * @param size
     * @return
     */
    @Override
    public Page<Post> getMyPostListByPage(String userId, Integer pageNo, Integer size) {
        Query query = new Query(Criteria.where("user_id").is(userId));
        long totalCount = mongoTemplate.count(query, Post.class);
        Pageable pageable = PageRequest.of(pageNo, size);
        query.with(pageable);
        List<Post> list = mongoTemplate.find(query, Post.class);
        return PageableExecutionUtils.getPage(list,pageable,()->totalCount);
    }

    /**
     * 获取该用户的帖子数量
     * @param userId
     * @return
     */
    @Override
    public Integer getUserPostCount(String userId) {
        Query query = new Query(Criteria.where("user_id").is(userId));
        long count = mongoTemplate.count(query, Post.class);
        return Integer.parseInt(String.valueOf(count));
    }

    /**
     * 获取该用户的帖子点赞量
     * @param userId
     * @return
     */
    @Override
    public Integer getUserPostLikeNumber(String userId) {
        int likeNumber=0;
        List<Post> posts = postRepository.getPostsByUserId(userId);
        for(Post post:posts){
            likeNumber+=post.getLikeNum();
        }
        return likeNumber;
    }

    /**
     * 根据帖子id查找帖子
     * @param postId
     * @return
     */
    @Override
    public Post getPostById(String postId) {
        return postRepository.getPostById(postId);
    }
}
