package com.user.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ikun.pojo.User;
import com.user.dao.UserMapper;
import com.user.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户逻辑业务接口实现层
 *
 * @author 9K
 * @create: 2023-07-21 13:13
 */

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
