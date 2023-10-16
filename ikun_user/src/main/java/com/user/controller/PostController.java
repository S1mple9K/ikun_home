package com.user.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ikun.data.CareData;
import com.ikun.data.CommentData;
import com.ikun.data.PostData;
import com.ikun.pojo.*;
import com.ikun.request.Result;
import com.ikun.return_.PostReturn;
import com.ikun.utils.SmsCodeUtil;
import com.user.repository.*;
import com.user.service.*;
import com.user.utils.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 帖子控制层
 *
 * @author 9K
 * @create: 2023-07-26 14:48
 */

@RestController
@RequestMapping("/api/post")
public class PostController {

    /**
     * 注入PostService
     */
    @Autowired
    private PostService postService;

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
     * 注入CareService
     */
    @Autowired
    private CareService careService;

    /**
     * 注入ReportService
     */
    @Autowired
    private ReportService reportService;

    /**
     * 注入CommentService
     */
    @Autowired
    private CommentService commentService;

    /**
     * 注入ReportDataService
     */
    @Autowired
    private ReportDataService reportDataService;

    /**
     * 注入ComLikeService
     */
    @Autowired
    private ComLikeService comLikeService;

    /**
     * 注入PostRepository
     */
    @Autowired
    private PostRepository postRepository;

    /**
     * 注入LikeRepository
     */
    @Autowired
    private LikeRepository likeRepository;

    /**
     * 注入CollectRepository
     */
    @Autowired
    private CollectRepository collectRepository;

    /**
     * 注入ReportDataRepository
     */
    @Autowired
    private ReportDataRepository reportDataRepository;

    /**
     * 注入TagRepository
     */
    @Autowired
    private TagRepository tagRepository;

    /**
     * 注入CommentRepository
     */
    @Autowired
    private CommentRepository commentRepository;

    /**
     * 注入OOS
     */
    @Autowired
    private OSS ossClient;

    /**
     * 存放帖子图片
     */
    public List<String> images = new ArrayList<>();

    /**
     * 存放用户位置信息
     */
    public static String ipLocation = "";

    /**
     * 标记是否查询过IP
     */
    public static Boolean queryIpFlag = false;

    /**
     * 获取帖子列表
     *
     * @return
     */
    @GetMapping("/getPostList/{userId}/{pageNo}/{size}")
    public Result getPostList(@PathVariable String userId, @PathVariable Integer pageNo, @PathVariable Integer size) {
        List<Post> postList = postService.getAllPostListByPage(pageNo - 1, size);
        if(postList.size()==0){
            return Result.fail("没有找到帖子");
        }
        List<PostReturn> returnList = getPostBaseInfo(userId, postList);
        if (!queryIpFlag) {
            new Thread(() -> {
                ipLocation = IpUtil.getIpLocation();
            }).start();
        }
        return Result.ok(returnList);
    }

    /**
     * 获取我发布的帖子列表
     *
     * @return
     */
    @GetMapping("/getMyPostList/{userId}/{otherUserId}/{pageNo}/{size}")
    public Result getMyPostList(@PathVariable String userId, @PathVariable String otherUserId, @PathVariable Integer pageNo, @PathVariable Integer size) {
        Page<Post> page = postService.getMyPostListByPage(otherUserId, pageNo - 1, size);
        List<Post> postList = page.getContent();
        if(postList.size()==0){
            return Result.fail("您没有发布帖子");
        }
        List<PostReturn> returnList = getPostBaseInfo(userId, postList);
        return Result.ok(returnList);
    }

    /**
     * 查看收藏的帖子列表
     */
    @GetMapping("/showCollectPostList/{userId}/{pageNo}/{size}")
    public Result showCollectPostList(@PathVariable String userId, @PathVariable Integer pageNo, @PathVariable Integer size) {
        //1.查询mongodb collect表中的符合userId的postId集合
        List<Collect> collectList = collectService.showUserCollectPost(userId);
        if(collectList.size()==0){
            return Result.fail("您没有收藏帖子");
        }
        //根据collectList集合中的数据查询帖子并且遵循pageNo和size
        List<Post> posts = new ArrayList<>();
        for (Collect collect : collectList) {
            //根据帖子id查找帖子
            Post post = postService.getPostById(collect.getPostId());
            if (post != null) {
                posts.add(post);
            }
        }
        int startIndex = (pageNo - 1) * size;
        int endIndex = Math.min(startIndex + size, posts.size());
        List<Post> paginatedPosts = posts.subList(startIndex, endIndex);
        List<PostReturn> list = getPostBaseInfo(userId, paginatedPosts);
        return Result.ok(list);
    }

    /**
     * 封装重复操作(集合)
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
                        post.setTagId(parentTag.getId());
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

        //排序
        Collections.sort(returnList, new Comparator<PostReturn>() {
            @Override
            public int compare(PostReturn o1, PostReturn o2) {
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

        return returnList;
    }

    /**
     * 封装重复操作(对象)
     */
    private PostReturn getPostBaseInfoObj(String userId, Post post) {
        // 根据用户id和帖子id查询点赞表,查看用户是否点赞了该帖子
        Integer result1 = likeService.showUserLikePostByUserIdAndPostId(userId, post.getId());
        // 根据用户id和帖子id查询收藏表,查看用户是否收藏了该帖子
        Integer result2 = collectService.showUserCollectPostByUserIdAndPostId(userId, post.getId());
        // 查询出全部标签
        List<Tag> tagList = tagRepository.findAll();
        // 查询出该用户关注
        List<Care> careList = careService.list(new QueryWrapper<Care>().eq("user_id", userId).eq("post_user_id", post.getUserId()));
        //深拷贝
        PostReturn postReturn = new PostReturn(
                post.getId(), post.getUserId(), post.getNickName(), post.getPhoto(), post.getContent(), post.getImages(), post.getViewNum(),
                post.getLikeNum(), post.getCommentNum(), post.getCollectNum(), post.getTagId(), post.getReleaseTime(), post.getWeight(), null, 0, 0, 0
        );

        //点赞信息
        if (result1 == 1) {
            postReturn.setIsLike(1);
        }

        if (result2 == 1) {
            postReturn.setIsCollect(1);
        }

        //标签信息
        for (Tag tag : tagList) {
            if (postReturn.getTagId().equals(tag.getId())) {
                if (tag.getTagName().equals("全部")) {
                    //根据它的父id找到大分类使用大分类的名称
                    Optional<Tag> optional = tagRepository.findById(tag.getParentId());
                    Tag parentTag = optional.get();
                    postReturn.setTagName(parentTag.getTagName());
                    postReturn.setTagId(parentTag.getId());
                } else {
                    postReturn.setTagName(tag.getTagName());
                }
            }
        }

        //关注信息
        for (Care care : careList) {
            if (postReturn.getUserId().equals(care.getPostUserId())) {
                postReturn.setIsCare(1);
            }
        }
        return postReturn;
    }

    /**
     * 上传帖子的图片
     */
    @PostMapping("/publishPhoto")
    public Result publishPhoto(@RequestParam("file") MultipartFile[] files) {
        for (int i = 0; i < files.length; i++) {
            //生成新的文件名存入阿里oss
            String originalFilename = files[i].getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = System.currentTimeMillis() + fileExtension;
            // 将文件存储到阿里oss
            updateImage(newFilename, files[i]);
            // 获取该文件url并存入数据库
            Result result = loadImage("ikun-net", newFilename);
            String url = String.valueOf(result.getData());
            images.add(url);
        }
        return Result.ok();
    }

    /**
     * 发布帖子
     */
    @PostMapping("/publishPost")
    public Result publishPost(@RequestBody PostData postData) {
        //保存到数据库
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Post post = postRepository.save(new Post(null, postData.getUserId(), postData.getNickName(), postData.getPhoto(), postData.getContent(),
                images.toArray(new String[images.size()]), 0, 0, 0, 0, postData.getTagId(), format.format(System.currentTimeMillis()), 0));
        // 清除集合
        images.clear();
        return post.getId() != null ? Result.ok("发布成功") : Result.fail("发布失败");
    }

    /**
     * 删除帖子
     */
    @GetMapping("/deletePost/{postId}")
    public Result deletePost(@PathVariable String postId) {
        // 删除阿里云oss中对应该帖子的图片
        // 查找出该帖子对应的图片
        Optional<Post> result = postRepository.findById(postId);
        // 获取结果
        String[] img = result.get().getImages();
        //循环删除
        for (String url : img) {
            removeOssImageByUrl(url);
        }
        // 删除帖子下的所有评论,点赞,收藏,举报
        commentService.deleteCommentByPostId(postId);
        likeService.deleteLikeByPostId(postId);
        reportDataService.deleteReportByPostId(postId);
        comLikeService.deleteComLikeByPostId(postId);
        //删除帖子
        postRepository.deleteById(postId);
        return Result.ok("删除成功");
    }

    /**
     * 用户点赞
     *
     * @param postId 帖子id
     * @param userId 用户id
     * @param isLike 是否点赞,区别点赞还是取消点赞
     */
    @GetMapping("/likePost/{postId}/{userId}/{isLike}")
    public Result likePost(@PathVariable String postId, @PathVariable String userId,
                           @PathVariable Integer isLike) {
        //如果是点赞
        if (isLike == 1) {
            //往点赞表中加入一条数据
            likeRepository.insert(new Like(null, postId, userId));
            //将该帖子的点赞数+1
            postService.addNum(postId, "like_num");
            return Result.ok("点赞成功");
        } else {
            //如果是取消点赞
            //从点赞表中删除点赞数据
            likeService.cancelLike(postId, userId);
            //将该帖子的点赞数-1
            postService.subtractNum(postId, "like_num", 1);
            return Result.ok("取消点赞成功");
        }
    }

    /**
     * 用户收藏
     *
     * @param postId    帖子id
     * @param userId    用户id
     * @param isCollect 是否收藏,区别收藏还是取消收藏
     */
    @GetMapping("/collectPost/{postId}/{userId}/{isCollect}")
    public Result collectPost(@PathVariable String postId, @PathVariable String userId,
                              @PathVariable Integer isCollect) {
        //如果是收藏
        if (isCollect == 1) {
            //往收藏表中加入一条数据
            collectRepository.insert(new Collect(null, postId, userId));
            //将该帖子的收藏数+1
            postService.addNum(postId, "collect_num");
            return Result.ok("收藏成功");
        } else {
            //如果是取消收藏
            //从收藏表中删除收藏数据
            collectService.cancelCollect(postId, userId);
            //将该帖子的收藏数-1
            postService.subtractNum(postId, "collect_num", 1);
            return Result.ok("取消收藏成功");
        }
    }

    /**
     * 用户关注
     */
    @PostMapping("/careUser")
    public Result careUser(@RequestBody CareData careData) {
        //关注
        if (careData.getIsCare() == 1) {
            //关注表中添加数据
            careService.save(new Care(null, careData.getUserId(), careData.getUserNickName(), careData.getPostUserId(), careData.getPostNickName(), careData.getPhoto()));
            return Result.ok("关注成功");
        } else {
            //取关
            //根据被取关者id和取关者id删除关注表中数据
            careService.remove(new QueryWrapper<Care>().eq("user_id", careData.getUserId()).eq("post_user_id", careData.getPostUserId()));
            return Result.ok("取关成功");
        }
    }

    /**
     * 添加浏览量
     */
    @GetMapping("/addViewNum/{postId}")
    public Result addViewNum(@PathVariable String postId) {
        // 将该帖子的浏览数+1
        postService.addNum(postId, "view_num");
        return Result.ok();
    }

    /**
     * 获取举报标签
     */
    @GetMapping("/getReportTagList")
    public Result getReportTagList() {
        List<Report> list = reportService.list();
        return Result.ok(list);
    }

    /**
     * 举报帖子
     */
    @PostMapping("/report")
    public Result report(@RequestBody ReportData reportData) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(System.currentTimeMillis());
        ReportData save = reportDataRepository.save(new ReportData(reportData.getId(), reportData.getPostId(),
                reportData.getPostUserId(), reportData.getPostUserNickName(), reportData.getUserId(), reportData.getNickName(),
                reportData.getPhoto(), reportData.getReportTag(), reportData.getContent(),
                date, reportData.getIsView(), reportData.getIsHandle()));
        return Result.ok("举报成功");
    }

    /**
     * 评论帖子
     */
    @PostMapping("/comment")
    public Result comment(@RequestBody CommentData commentData) {
        //将评论信息存入评论表中
        if (commentData.getAncestorsId() == 0 && commentData.getParentId() == 0) {
            //评论
            // 主键id
            long id = System.currentTimeMillis() + SmsCodeUtil.generateEight();
            // 评论时间
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = format.format(System.currentTimeMillis());
            commentRepository.save(new Comment(id, commentData.getPostId(), commentData.getComName(), commentData.getUserId(), commentData.getNickName(),
                    commentData.getPhoto(), commentData.getContent(), date, commentData.getAncestorsId(), commentData.getParentId(), 0, 0));
            //将该帖子的评论字段＋1
            postService.addNum(commentData.getPostId(), "comment_num");
            return Result.ok("评论成功");
        } else {
            //回复
            // 主键id
            long id = System.currentTimeMillis() + SmsCodeUtil.generateEight();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = format.format(System.currentTimeMillis());
            commentRepository.save(new Comment(id, commentData.getPostId(), commentData.getComName(), commentData.getUserId(), commentData.getNickName(),
                    commentData.getPhoto(), commentData.getContent(), date, commentData.getAncestorsId(), commentData.getParentId(), 0, 0));
            //将该帖子的评论字段＋1
            postService.addNum(commentData.getPostId(), "comment_num");
            //将该评论的回复量+1
            commentService.addNum(commentData.getAncestorsId(), "reply_num");
            commentService.addNum(commentData.getParentId(), "reply_num");
            return Result.ok("回复成功");
        }
    }

    /**
     * 评论页获取帖子最新信息
     */
    @GetMapping("/getFreshPostInfo/{userId}/{postId}")
    public Result getFreshPostInfo(@PathVariable String userId, @PathVariable String postId) {
        Optional<Post> optional = postRepository.findById(postId);
        Post post = optional.get();
        PostReturn postReturn = getPostBaseInfoObj(userId, post);
        return Result.ok(postReturn);
    }


    /**
     * 上传图片
     */
    private boolean updateImage(String fileName, @RequestParam("file") MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentDisposition("attachment;filename=" + fileName);
        try {
            ossClient.putObject("ikun-net", fileName, file.getInputStream(), metadata);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取图片url
     *
     * @param bucketName
     * @param objectName
     * @return
     */
    public Result loadImage(@PathVariable String bucketName, @PathVariable String objectName) {
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, objectName);
        //设置过期时间
        request.setExpiration(new Date(System.currentTimeMillis() + 36000000 * 100000));
        String url = ossClient.generatePresignedUrl(request).toString();
        //将httpUrl转为https
        String httpsUrl = url.replace("http://", "https://");
        return Result.ok(httpsUrl);
    }

    /**
     * 根据url删除阿里oss中的图片
     */
    private void removeOssImageByUrl(String url) {
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        fileName = fileName.substring(0, fileName.lastIndexOf(".") + 4);
        // 删除文件
        ossClient.deleteObject("ikun-net", fileName);
    }

}
