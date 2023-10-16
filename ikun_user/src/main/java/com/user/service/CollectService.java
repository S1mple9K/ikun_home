package com.user.service;

import com.ikun.pojo.Collect;

import java.util.List;

/**
 * 收藏表业务逻辑接口层
 * @author 9K
 * @create: 2023-07-27 15:13
 */
public interface CollectService {

    /**
     * 删除收藏信息
     * @param postId
     * @param userId
     */
    void cancelCollect(String postId, String userId);

    /**
     * 查询用户收藏的帖子
     * @param userId
     * @return
     */
    List<Collect> showUserCollectPost(String userId);

    /**
     * 根据用户id和帖子id查询收藏表,查看用户是否收藏了该帖子
     * @param userId
     * @param postId
     * @return
     */
    Integer showUserCollectPostByUserIdAndPostId(String userId, String postId);
}
