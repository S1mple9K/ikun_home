package com.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ikun.pojo.*;
import com.ikun.request.Result;
import com.ikun.return_.PostReturn;
import com.user.repository.TagRepository;
import com.user.service.CareService;
import com.user.service.CollectService;
import com.user.service.LikeService;
import com.user.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 关注控制层
 *
 * @author 9K
 * @create: 2023-07-28 14:32
 */
@RestController
@RequestMapping("/api/care")
public class CareController {

    /**
     * 注入CareService
     */
    @Autowired
    private CareService careService;

    /**
     * 注入LikeService
     */
    @Autowired
    private LikeService likeService;

    /**
     * 注入CollectService
     */
    @Autowired
    private CollectService collectService;

    /**
     * 注入TagRepository
     */
    @Autowired
    private TagRepository tagRepository;

    /**
     * 注入PostService
     */
    @Autowired
    private PostService postService;

    /**
     * 获取关注人列表
     *
     * @param userId 用户id
     * @return
     */
    @GetMapping("/getCareUserList/{userId}")
    public Result getCareUserList(@PathVariable String userId) {
        //根据id获取关注人列表
        List<Care> careList = careService.list(new QueryWrapper<Care>().eq("user_id", userId));
        //根据发帖人的id查询出他的帖子
        return Result.ok(careList);
    }

    /**
     * 获取我关注的帖子列表
     */
    @GetMapping("/getCareUserPostList/{userId}")
    public Result getCareUserPostList(@PathVariable String userId) {
        //根据id获取关注人列表
        List<Care> careList = careService.list(new QueryWrapper<Care>().eq("user_id", userId));
        //存放所有关注者的帖子集合
        List<List<PostReturn>> bigList = new ArrayList<>();
        //根据关注人列表id查询他们的帖子
        careList.stream().forEach(item -> {
            //查询帖子
            List<Post> postList = postService.getCareList(item.getPostUserId());
            List<PostReturn> returnList = getPostBaseInfo(userId, postList);
            bigList.add(returnList);
        });
        //将多层集合转为一个集合
        List<PostReturn> returnList = bigList.stream().flatMap(List::stream).collect(Collectors.toList());
        //日期排序,时间越晚,越上面
        Collections.sort(returnList, new Comparator<PostReturn>() {
            @Override
            public int compare(PostReturn o1, PostReturn o2) {
                // 按照权重值进行排序
                if (o1.getWeight() > o2.getWeight()) {
                    return -1; // o1的权重值大于o2的权重值，o1排在o2前面
                } else if (o1.getWeight() < o2.getWeight()) {
                    return 1; // o1的权重值小于o2的权重值，o1排在o2后面
                } else {
                    return o2.getReleaseTime().compareTo(o1.getReleaseTime());
                }
            }
        });
        return Result.ok(returnList);
    }

    /**
     * 获取关注的用户帖子列表
     */
    @GetMapping("/getUserPost/{postUserId}/{userId}")
    public Result getUserPost(@PathVariable String postUserId, @PathVariable String userId) {
        List<Post> careList = postService.getCareList(postUserId);
        List<PostReturn> returnList = getPostBaseInfo(userId, careList);
        //日期排序,时间越晚,越上面
        Collections.sort(returnList, new Comparator<PostReturn>() {
            @Override
            public int compare(PostReturn o1, PostReturn o2) {
                return o2.getReleaseTime().compareTo(o1.getReleaseTime());
            }
        });
        return Result.ok(returnList);
    }

    /**
     * 封装重复操作
     */
    private List<PostReturn> getPostBaseInfo(String userId, List<Post> postList) {
        // 根据用户id遍历点赞表,查看用户是否点赞了该帖子
        List<Like> likeList = likeService.showUserLikePost(userId);
        // 根据用户id遍历收藏表,查看用户是否收藏了该帖子
        List<Collect> collectList = collectService.showUserCollectPost(userId);
        // 查询出全部标签
        List<Tag> tagList = tagRepository.findAll();
        // 查询出该用户关注
        List<Care> careList = careService.list(new QueryWrapper<Care>().eq("user_id", userId));
        //创建结果集合
        List<PostReturn> returnList = new ArrayList<>(postList.size());
        //深拷贝
        returnList = postList.stream().map(item -> new PostReturn(
                        item.getId(), item.getUserId(), item.getNickName(), item.getPhoto(), item.getContent(), item.getImages(), item.getViewNum(),
                        item.getLikeNum(), item.getCommentNum(), item.getCollectNum(), item.getTagId(), item.getReleaseTime(), item.getWeight(), null, 0, 0, 0
                ))
                .collect(Collectors.toList());

        //点赞信息
        for (PostReturn post : returnList) {
            for (Like like : likeList) {
                if (post.getId().equals(like.getPostId())) {
                    post.setIsLike(1);
                }
            }
        }
        //收藏信息
        for (PostReturn post : returnList) {
            for (Collect collect : collectList) {
                if (post.getId().equals(collect.getPostId())) {
                    post.setIsCollect(1);
                }
            }
        }


        //标签信息
        for (PostReturn post : returnList) {
            for (Tag tag : tagList) {
                if (post.getTagId().equals(tag.getId())) {
                    if (tag.getTagName().equals("全部")) {
                        //根据它的父id找到大分类使用大分类的名称
                        Optional<Tag> optional = tagRepository.findById(tag.getParentId());
                        Tag parentTag = optional.get();
                        post.setTagName(parentTag.getTagName());
                    } else {
                        post.setTagName(tag.getTagName());
                    }
                }
            }
        }

        //关注信息
        for (PostReturn post : returnList) {
            for (Care care : careList) {
                if (post.getUserId().equals(care.getPostUserId())) {
                    post.setIsCare(1);
                }
            }
        }
        return returnList;
    }

}
