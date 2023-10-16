package com.user.service;

import com.ikun.pojo.Comment;
import com.ikun.return_.UserInfoReturn;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 评论业务逻辑接口层
 * @author 9K
 * @create: 2023-07-30 15:54
 */

public interface CommentService {

    /**
     * 查询帖子下的所有评论
     * @param postId
     * @return
     */
    List<Comment> getPostCommentList(String postId ,Long ancestorsId,Long parentId);

    /**
     * 将该帖子的某个字段值+1
     * @param parentId
     * @param key
     */
    void addNum(Long parentId, String key);

    /**
     * 将该帖子的某个字段值-1
     * @param parentId
     * @param key
     */
    void subtractNum(Long parentId, String key);

    /**
     * 查询评论下的所有回复
     * @param postId
     * @param ancestorsId
     * @return
     */
    List<Comment> getComReplyList(String postId, Long ancestorsId);

    /**
     * 根据ancestorsId删除评论回复
     * @param comId
     */
    Long deleteCommentByAncestorsId(Long comId);

    /**
     * 根据postId删除评论信息
     * @param postId
     */
    void deleteCommentByPostId(String postId);

    /**
     * 分页查询帖子下的所有评论
     * @param postId
     * @param ancestorsId
     * @param parentId
     * @param pageNo
     * @param size
     * @return
     */
    Page<Comment> getPostCommentListByPage(String postId, Long ancestorsId, Long parentId, Integer pageNo, Integer size);

    /**
     * 评论表中的用户信息做修改
     *
     * @param userInfoReturn
     * @return
     */
    UpdateResult updateUserInfo(UserInfoReturn userInfoReturn);
}
