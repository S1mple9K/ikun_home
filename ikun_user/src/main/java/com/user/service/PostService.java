package com.user.service;

import com.ikun.pojo.Post;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 帖子业务逻辑接口层
 *
 * @author 9K
 * @create: 2023-07-26 14:03
 */

public interface PostService {

    /**
     * 修改用户昵称
     *
     * @param userId
     * @param nickName
     */
    UpdateResult updateUserNickName(String userId, String nickName);

    /**
     * 修改用户头像
     *
     * @param userId
     * @param url
     */
    void updateUserPhoto(String userId, String url);

    /**
     * 获取帖子列表
     *
     * @return
     */
    List<Post> getPostList();

    /**
     * 获取我发布的帖子列表
     *
     * @param userId
     * @return
     */
    List<Post> getMyPostList(String userId);

    /**
     * 将帖子的某个字段+1
     *
     * @param postId
     * @param key
     */
    void addNum(String postId, String key);

    /**
     * 将帖子的某个字段-1
     *
     * @param postId
     * @param key
     */
    void subtractNum(String postId, String key, Integer num);

    /**
     * 获取关注用户帖子
     *
     * @param postUserId
     * @return
     */
    List<Post> getCareList(String postUserId);

    /**
     * 分页查询所有帖子
     *
     * @param pageNo
     * @param size
     * @return
     */
    List<Post> getAllPostListByPage(Integer pageNo, Integer size);

    /**
     * 分页查询我发布的帖子
     *
     * @param userId
     * @param pageNo
     * @param size
     * @return
     */
    Page<Post> getMyPostListByPage(String userId, Integer pageNo, Integer size);

    /**
     * 获取该用户的帖子数量
     *
     * @param userId
     */
    Integer getUserPostCount(String userId);

    /**
     * 获取该用户的帖子点赞量
     *
     * @param userId
     * @return
     */
    Integer getUserPostLikeNumber(String userId);

    /**
     * 根据帖子id查找帖子
     * @param postId
     */
    Post getPostById(String postId);
}
