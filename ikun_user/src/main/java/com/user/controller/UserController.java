package com.user.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ikun.data.LoginData;
import com.ikun.data.OpinionData;
import com.ikun.data.PasswordData;
import com.ikun.pojo.Care;
import com.ikun.pojo.Online;
import com.ikun.pojo.User;
import com.ikun.request.Result;
import com.ikun.return_.UserInfoReturn;
import com.ikun.return_.UserOtherInfoReturn;
import com.ikun.return_.UserReturn;
import com.mongodb.client.result.UpdateResult;
import com.user.email.EmailSend;
import com.user.service.*;
import com.user.utils.AliyunSmsSender;
import com.user.utils.SmsCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 用户相关控制层
 *
 * @author 9K
 * @create: 2023-07-23 13:29
 */

@RestController
@RequestMapping("/api/user")
public class UserController {

    /**
     * 注入UserService
     */
    @Autowired
    private UserService userService;

    /**
     * 注入CareService
     */
    @Autowired
    private CareService careService;

    /**
     * 注入redisTemplate
     */
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 注入postService
     */
    @Autowired
    private PostService postService;

    /**
     * 注入commentService
     */
    @Autowired
    private CommentService commentService;

    /**
     * 注入短信发送类
     */
    @Autowired
    private EmailSend emailSend;

    /**
     * 注入OOS
     */
    @Autowired
    private OSS ossClient;

    /**
     * 用户验证码登录
     *
     * @return
     */
    @GetMapping("/login/code/{phone}/{code}")
    public Result userLoginByCode(@PathVariable String phone, @PathVariable String code, HttpSession session) {
        //获取用户输入的验证码,跟redis中存储的验证码进行比对
        Object smsCode = redisTemplate.opsForValue().get(phone);
        if (smsCode == null) {
            return Result.fail("验证码已过期或不存在");
        } else {
            if (smsCode.equals(code)) {
                //判断用户是不是初次登录
                //根据手机号查询数据库
                User isExist = userService.getOne(new QueryWrapper<User>().eq("phone", phone));
                if (isExist != null) {
                    if (isExist.getStatus() != 1) {
                        return Result.fail("该用户已被禁用");
                    } else {
                        return Result.ok(new UserReturn(isExist.getUserId(), isExist.getNickName(), null, isExist.getPhoto(), 0, isExist.getStatus()));
                    }
                } else {
                    //调用注册用户默认信息
                    UserReturn userReturn = registerUser(phone);
                    return userReturn != null ? Result.ok(userReturn) : Result.ok("登录失败");
                }
            } else {
                return Result.fail("验证码错误");
            }
        }
    }

    /**
     * 用户密码登录
     *
     * @return
     */
    @PostMapping("/login/pwd")
    public Result userLoginByPwd(@RequestBody LoginData loginData, HttpSession session) {
        //根据手机号查询用户
        User user = userService.getOne(new QueryWrapper<User>().eq("phone", loginData.getPhone()));
        if (user == null) {
            //用户不存在
            return Result.fail("用户不存在");
        } else {
            //将用户输入的密码进行加密,与数据库的密码进行比对
            byte[] encode = Base64.getEncoder().encode(loginData.getPassword().getBytes(StandardCharsets.UTF_8));
            String password = new String(encode, StandardCharsets.UTF_8);
            if (password.equals(user.getPassword())) {
                //比对成功
                if (user.getStatus() != 1) {
                    return Result.fail("该用户已被禁用");
                } else {
                    return Result.ok(new UserReturn(user.getUserId(), user.getNickName(), null, user.getPhoto(), 0, user.getStatus()));
                }
            } else {
                return Result.fail("密码错误");
            }
        }
    }

    /**
     * 根据用户输入的名称模糊查询用户
     */
    @GetMapping("/queryUser/{keyword}")
    public Result queryUser(@PathVariable String keyword) {
        //根据keyword模糊查询数据库的用户表
        //1.构建查询条件
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like(keyword != null, "nick_name", keyword);
        //2.查询数据库
        List<User> userList = userService.list(wrapper);
        for (User user : userList) {
            user.setId(null);
            user.setPhone("");
            user.setPassword("");
        }
        //3.返回结果
        return userList.size() != 0 ? Result.ok(userList) : Result.fail("查询不到该用户");
    }

    /**
     * 用户建议
     */
    @PostMapping("/userOpinion")
    public Result userOpinion(@RequestBody OpinionData opinionData) {
        boolean b = emailSend.sendEmail(opinionData.getUserId(),opinionData.getNickName(),opinionData.getOpinion());
        return b ? Result.ok("提交成功,感谢你的反馈") : Result.fail("提交失败,请稍后再试");
    }

    /**
     * 获取验证码
     */
    @GetMapping("/getCode/{phone}")
    public Result getCode(@PathVariable String phone) {
        if(isValidPhoneNumber(phone)) {
            //生成六位数验证码
            Integer code = SmsCodeUtil.generateSix();
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
            //等会放入redis缓存中设置过期时间
            String result = AliyunSmsSender.sendSms(phone, String.valueOf(code));
            return result == "ok" ? Result.ok("获取成功") : Result.fail("获取失败");
        }else{
            return Result.fail("手机号有误");
        }
    }

    /**
     * 用户修改密码
     */
    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody PasswordData passwordData) {
        //根据用户id查找用户
        User user = userService.getOne(new QueryWrapper<User>().eq("user_id", passwordData.getUserId()));
        //使用加密算法加密原密码
        byte[] encode = Base64.getEncoder().encode(passwordData.getOldPwd().getBytes(StandardCharsets.UTF_8));
        String password = new String(encode, StandardCharsets.UTF_8);
        if (!user.getPassword().equals(password)) {
            return Result.fail("原密码错误");
        } else {
            //将新密码加密存入数据库
            //使用加密算法加密新密码
            encode = Base64.getEncoder().encode(passwordData.getNewPwd().getBytes(StandardCharsets.UTF_8));
            password = new String(encode, StandardCharsets.UTF_8);
            userService.update(new UpdateWrapper<User>().set("password", password).eq("user_id", passwordData.getUserId()));
            return Result.ok("修改成功,请重新登录");
        }
    }

    /**
     * 获取当前用户状态值和删除值
     */
    @GetMapping("/getStatus/{userId}")
    public Result getStatus(@PathVariable String userId) {
        User user = userService.getOne(new QueryWrapper<User>().eq("user_id", userId));
        if (user.getStatus() == 0 || user.getIsDelete() == 1 || user.getStatus() == 0 && user.getIsDelete() == 1) {
            return Result.ok(0);
        } else {
            return Result.ok(1);
        }
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/getUserInfo/{userId}")
    public Result getUserInfo(@PathVariable String userId) {
        //根据userId查询数据库
        User user = userService.getOne(new QueryWrapper<User>().eq("user_id", userId));
        //获取用户位置信息
        String ip = PostController.ipLocation;
        UserInfoReturn userInfoReturn = new UserInfoReturn(userId, user.getNickName(), user.getSex()==1?"男":"女", user.getAge(), user.getSign(), user.getCreateTime(), user.getPhoto(), ip);
        return Result.ok(userInfoReturn);
    }

    /**
     * 获取用户其他信息：获赞数,关注数,粉丝数
     */
    @GetMapping("/getUserOtherInfo/{userId}")
    public Result getUserOtherInfo(@PathVariable String userId) {
        //获取该用户的帖子数量及点赞量
        Integer postCount = postService.getUserPostCount(userId);
        Integer likeNumber = postService.getUserPostLikeNumber(userId);
        //获取用户关注数和粉丝数
        QueryWrapper<Care> wrapper = new QueryWrapper<>();
        wrapper.eq(userId != null, "user_id", userId);
        long careNumber = careService.count(wrapper);
        wrapper.eq(userId != null, "post_user_id", userId);
        long fansNumber = careService.count(wrapper);
        UserOtherInfoReturn infoReturn = new UserOtherInfoReturn(postCount, likeNumber, Integer.parseInt(String.valueOf(careNumber))
                , Integer.parseInt(String.valueOf(fansNumber)));
        return Result.ok(infoReturn);
    }

    /**
     * 根据用户id修改信息
     */
    @PostMapping("/updateUserInfo")
    public Result updateUserInfo(@RequestBody UserInfoReturn userInfoReturn) {
        if(userInfoReturn!=null) {
            int sex;
            if(userInfoReturn.getSex().equals("男")){
                sex=1;
            }else if(userInfoReturn.getSex().equals("女")){
                sex=0;
            }else{
                return Result.fail("修改失败,数据有误");
            }
            boolean result = userService.update(new UpdateWrapper<User>().set("nick_name", userInfoReturn.getNickName()).set("sex", sex)
                    .set("age", userInfoReturn.getAge()).set("sign", userInfoReturn.getSign()).eq("user_id", userInfoReturn.getUserId()));
            // 将mongodb post表中的用户信息也做更改
            postService.updateUserNickName(userInfoReturn.getUserId(), userInfoReturn.getNickName());
            // 将mongodb 评论表中的用户信息做修改
            UpdateResult updateResult = commentService.updateUserInfo(userInfoReturn);
            return result ? Result.ok(new UserReturn(userInfoReturn.getUserId(), userInfoReturn.getNickName(), null, userInfoReturn.getPhoto(), 0, 1)) : Result.fail("修改失败");
        }else{
            return Result.fail("修改失败");
        }
    }

    /**
     * 用户上传头像
     *
     * @param file   源文件
     * @param userId
     * @return
     */
    @PostMapping("/updatePhoto")
    public Result updatePhoto(@RequestParam("file") MultipartFile file, @RequestParam("userId") String userId,
                              @RequestParam("oldPhoto") String oldPhotoUrl) {
        if(userId==null||userId.isEmpty()){
            return Result.fail("禁止上传");
        }else {
            // 根据用户ID从缓存中获取上次上传头像的时间
            String lastUploadTimeKey = "lastUploadTime:" + userId;
            Long lastUploadTime = (Long) redisTemplate.opsForValue().get(lastUploadTimeKey);

            // 如果上次上传时间存在且在一天内，返回错误提示
            if (lastUploadTime != null && System.currentTimeMillis() - lastUploadTime < 24 * 60 * 60 * 1000) {
                return Result.updateFail("每天只能上传一次头像");
            }else {
                // 删除阿里oss中用户原来的头像
                // 从url中解析出文件名
                String fileName = oldPhotoUrl.substring(oldPhotoUrl.lastIndexOf("/") + 1);
                fileName = fileName.substring(0, fileName.lastIndexOf(".") + 4);
                // 删除文件
                ossClient.deleteObject("ikun-net", fileName);
                // 为文件设置名称
                String originalFilename = file.getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String newFilename = System.currentTimeMillis() + fileExtension;
                // 将文件存储到阿里oss
                updateImage(newFilename, file);
                // 获取该文件url并存入数据库
                Result result = loadImage("ikun-net", newFilename);
                String url = String.valueOf(result.getData());
                // 根据用户id更新头像
                userService.update(new UpdateWrapper<User>().set("photo", url).eq("user_id", userId));
                // 将mongodb post表中的用户头像也做更改
                postService.updateUserPhoto(userId, url);
                // 将mysql 关注表的用户头像也做更改
                careService.update(new UpdateWrapper<Care>().set("photo", url).eq("post_user_id", userId));
                //存入缓存
                redisTemplate.opsForValue().set(lastUploadTimeKey, System.currentTimeMillis(), 24, TimeUnit.HOURS);
                return Result.ok();
            }
        }
    }

    /**
     * 用户注册成功生成的默认信息
     */
    private UserReturn registerUser(String phone) {
        //如果比对正确,让用户的信息注册进数据库,并为其设置一个默认密码
        Integer random = SmsCodeUtil.generateEight();
        String name = "用户" + String.valueOf(random);
        String uuidPwd = UUID.randomUUID().toString();
        //使用加密算法加密密码
        byte[] encode = Base64.getEncoder().encode(uuidPwd.getBytes(StandardCharsets.UTF_8));
        String password = new String(encode, StandardCharsets.UTF_8);
        //设置默认头像
        String photo = "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png";
        long millis = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = format.format(millis);
        User user = new User(null, String.valueOf(random), name, null, null, phone, password, null, photo, createTime, 1, 0, null, 0);
        //调用业务层的添加方法
        boolean result = userService.save(user);
        boolean save = false;
        //用户登录成功返回实体类
        return result && save ? new UserReturn(String.valueOf(random), name, uuidPwd, photo, 1, 1) : null;
    }

    /**
     * 获取用户名
     *
     * @param session
     * @return
     */
    @GetMapping("/getUsername")
    public Result getUsername(HttpSession session) {
        String username = (String) session.getAttribute("user");
        return Result.ok(username);
    }

    /**
     * 根据用户名获取头像
     */
    @GetMapping("/getPhoto/{nickName}")
    public Result getPhoto(@PathVariable String nickName) {
        //根据用户名查询数据库
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq(nickName != null, "nick_name", nickName);
        User user = userService.getOne(wrapper);
        return Result.ok(user);
    }

    /**
     * 读取图片
     */
    @GetMapping("/loadImage/{bucketName}/{objectName}")
    public Result loadImage(@PathVariable String bucketName, @PathVariable String objectName) {
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, objectName);
        // 设置过期时间为10年后的时间戳
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 10); // 添加10年
        Date expirationDate = calendar.getTime();
        request.setExpiration(expirationDate);
        String url = ossClient.generatePresignedUrl(request).toString();
        // 将httpUrl转为https
        String httpsUrl = url.replace("http://", "https://");
        return Result.ok(httpsUrl);
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
     * 手机号是否匹配
     */
    private boolean isValidPhoneNumber(String phone){
        if((phone!=null)&&(!phone.isEmpty())){
            return Pattern.matches("^1((34[0-8])|(8\\d{2})|(([35][0-35-9]|4[579]|66|7[35678]|9[1389])\\d{1}))\\d{7}$",phone);
        }
        return false;
    }
}
