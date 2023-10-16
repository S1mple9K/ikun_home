package com.user.service;

import com.ikun.pojo.Like;

import java.util.List;

/**
 * 点赞表业务逻辑接口层
 * @author 9K
 * @create: 2023-07-27 14:18
 */
public interface LikeService {
    /**
     * 根据帖子id和用户id取消点赞信息
     * @param postId
     * @param userId
     */
    void cancelLike(String postId, String userId);

    /**
     * 查询用户点赞的帖子
     *
     * @param userId
     * @return
     */
    List<Like> showUserLikePost(String userId);

    /**
     * 根据用户id和帖子id查询点赞表,查看用户是否点赞了该帖子
     * @param userId
     */
    Integer showUserLikePostByUserIdAndPostId(String userId,String postId);

    /**
     * 根据postId删除点赞
     * @param postId
     */
    void deleteLikeByPostId(String postId);
}
