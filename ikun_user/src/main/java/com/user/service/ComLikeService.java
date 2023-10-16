package com.user.service;

import com.ikun.pojo.ComLike;

import java.util.List;

/**
 * 评论点赞业务逻辑接口层
 * @author 9K
 * @create: 2023-07-31 19:57
 */
public interface ComLikeService {
    /**
     * 从点赞表中删除点赞数据
     * @param comId
     * @param userId
     */
    void cancelLike(Long comId, String userId);

    /**
     * 根据用户id遍历点赞表,查看用户是否点赞了该帖子
     * @param userId
     * @return
     */
    List<ComLike> showUserLikePost(String userId);

    /**
     * 根据评论id删除评论点赞信息
     * @param comId
     */
    void deleteComLikeByComId(Long comId);

    /**
     * 根据帖子id删除评论点赞信息
     * @param postId
     */
    void deleteComLikeByPostId(String postId);
}
