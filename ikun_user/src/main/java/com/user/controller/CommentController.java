package com.user.controller;

import com.ikun.pojo.ComLike;
import com.ikun.pojo.Comment;
import com.ikun.request.Result;
import com.ikun.return_.CommentReturn;
import com.user.repository.ComLikeRepository;
import com.user.repository.CommentRepository;
import com.user.service.ComLikeService;
import com.user.service.CommentService;
import com.user.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 评论控制层
 *
 * @author 9K
 * @create: 2023-07-30 15:51
 */

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    /**
     * 注入CommentService
     */
    @Autowired
    private CommentService commentService;

    /**
     * 注入ComLikeService
     */
    @Autowired
    private ComLikeService comLikeService;

    /**
     * 注入PostService
     */
    @Autowired
    private PostService postService;

    /**
     * 注入CommentRepository
     */
    @Autowired
    private CommentRepository commentRepository;

    /**
     * 注入ComLikeRepository
     */
    @Autowired
    private ComLikeRepository comLikeRepository;

    /**
     * 查询帖子下的所有评论
     */
    @GetMapping("/getPostCommentList/{postId}/{ancestorsId}/{parentId}/{userId}/{pageNo}/{size}")
    public Result getPostCommentList(@PathVariable String postId, @PathVariable Long ancestorsId, @PathVariable Long parentId,
                                     @PathVariable String userId, @PathVariable Integer pageNo, @PathVariable Integer size) {
        System.out.println("pageNo:"+pageNo+",size:"+size);
        Page<Comment> page = commentService.getPostCommentListByPage(postId, ancestorsId, parentId, pageNo, size);
        List<Comment> content = page.getContent();
        System.out.println("content.size() = " + content.size());
        System.out.println("-----");
        System.out.println("content = " + content);
        // 根据该用户id查询他点赞过的评论
        /*
            1.创建一个专门返回的类,需要标记当前用户是否点赞
            2.用户查询评论或回复需要传递当前用户id
            3.创建点赞表,当用户点赞就插入信息,取消就取消信息,同时更新评论表中该评论的数据
            4.查询到了原有集合,需要先由点赞数排序,点赞数如果一样就以发布时间排序
            5.根据用户id查询用户点赞的数据,查询到了数据与原有集合继续双重for循环遍历匹配,如果匹配成功则设置当前评论的是否点赞为已点赞
         */
        List<CommentReturn> returnList = getCommentBaseInfo(userId, content);
        return Result.ok(returnList);
    }

    /**
     * 查询评论下的所有回复
     */
    @GetMapping("/getComReplyList/{postId}/{ancestorsId}/{userId}")
    public Result getComReplyList(@PathVariable String postId, @PathVariable Long ancestorsId, @PathVariable String userId) {
        List<Comment> commentList = commentService.getComReplyList(postId, ancestorsId);
        List<CommentReturn> returnList = getCommentBaseInfo(userId, commentList);
        return Result.ok(returnList);
    }

    /**
     * 点赞/取消点赞评论
     */
    @GetMapping("/likeComment/{postId}/{comId}/{userId}/{isLike}")
    public Result likeComment(@PathVariable String postId, @PathVariable Long comId, @PathVariable String userId,
                              @PathVariable Integer isLike) {
        //如果是点赞
        if (isLike == 1) {
            //往点赞表中加入一条数据
            comLikeRepository.insert(new ComLike(null, postId, comId, userId));
            // 评论点赞数+1
            commentService.addNum(comId, "like_num");
            return Result.ok("点赞成功");
        } else {
            //如果是取消点赞
            //从点赞表中删除点赞数据
            comLikeService.cancelLike(comId, userId);
            // 评论点赞数-1
            commentService.subtractNum(comId, "like_num");
            return Result.ok("取消点赞成功");
        }
    }

    /**
     * 删除评论/回复
     */
    @GetMapping("/deleteComment/{postId}/{comId}/{ancestorsId}/{parentId}/{userId}/{delType}")
    public Result deleteComment(@PathVariable String postId, @PathVariable Long comId, @PathVariable Long ancestorsId, @PathVariable Long parentId, @PathVariable String userId, @PathVariable Integer delType) {
        if (delType == 0) {
            //删除评论
            commentRepository.deleteById(comId);
            //从评论表中删除,并删除以他为ancestorsId的回复
            Long aLong = commentService.deleteCommentByAncestorsId(comId);
            //删除评论点赞信息
            comLikeService.deleteComLikeByComId(comId);
            //更新帖子评论数:根据帖子id减去删除数
            postService.subtractNum(postId, "reply_num", Math.toIntExact(aLong));
        } else {
            if (ancestorsId == 0) {
                //删除一级回复
                //删除评论
                commentRepository.deleteById(comId);
                //将该父级评论回复数-1,该回复的parentId
                commentService.subtractNum(parentId, "reply_num");
                //帖子评论数-1
                postService.subtractNum(postId, "comment_num", 1);
                //删除评论点赞信息
                comLikeService.deleteComLikeByComId(comId);
            } else {
                //删除二级回复
                //删除评论
                commentRepository.deleteById(comId);
                //将该祖级评论回复数-1
                commentService.subtractNum(ancestorsId, "reply_num");
                //将该父级评论回复数-1
                commentService.subtractNum(parentId, "reply_num");
                //帖子评论数-1
                postService.subtractNum(postId, "comment_num", 1);
                //删除评论点赞信息
                comLikeService.deleteComLikeByComId(comId);
            }
        }
        return Result.ok("删除成功");
    }

    /**
     * 封装重复操作(集合)
     */
    private List<CommentReturn> getCommentBaseInfo(String userId, List<Comment> commentList) {
        // 根据用户id遍历点赞表,查看用户是否点赞了该帖子
        List<ComLike> comLikeList = comLikeService.showUserLikePost(userId);
        //创建结果集合
        List<CommentReturn> returnList = new ArrayList<>(commentList.size());
        //深拷贝
        returnList = commentList.stream().map(item -> new CommentReturn(
                        item.getId(), item.getPostId(), item.getComName(), item.getUserId(), item.getNickName(),
                        item.getPhoto(), item.getContent(), item.getTime(), item.getAncestorsId(), item.getParentId(), item.getLikeNum(),
                        item.getReplyNum(), 0
                ))
                .collect(Collectors.toList());

        //点赞信息
        for (CommentReturn commentReturn : returnList) {
            for (ComLike comLike : comLikeList) {
                if (commentReturn.getId().equals(comLike.getComId())) {
                    commentReturn.setIsLike(1);
                }
            }
        }
        //对时间进行处理
        for (CommentReturn commentReturn : returnList) {
            LocalDateTime dateTime = LocalDateTime.parse(commentReturn.getTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime now = LocalDateTime.now();
            long minutes = ChronoUnit.MINUTES.between(dateTime, now);
            long hours = ChronoUnit.HOURS.between(dateTime, now);
            long days = ChronoUnit.DAYS.between(dateTime, now);
            long months = ChronoUnit.MONTHS.between(dateTime, now);
            long years = ChronoUnit.YEARS.between(dateTime, now);
            if (years > 0) {
                commentReturn.setTime(years + "年前");
            } else if (months > 0) {
                commentReturn.setTime(months + "个月前");
            } else if (days > 0) {
                commentReturn.setTime(days + "天前");
            } else if (hours > 0) {
                commentReturn.setTime(hours + "小时前");
            } else if (minutes > 0) {
                commentReturn.setTime(minutes + "分钟前");
            } else {
                commentReturn.setTime("刚刚");
            }
        }

        Collections.sort(returnList, new Comparator<CommentReturn>() {
            @Override
            public int compare(CommentReturn o1, CommentReturn o2) {
                // 先按照点赞数排序
                int likeNumComparison = Integer.compare(o2.getLikeNum(), o1.getLikeNum());
                if (likeNumComparison != 0) {
                    return likeNumComparison;
                }

                // 如果点赞数相同，则按照时间排序
                return o2.getTime().compareTo(o1.getTime());
            }
        });
        return returnList;
    }
}
