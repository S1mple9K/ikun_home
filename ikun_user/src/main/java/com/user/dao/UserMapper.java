package com.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ikun.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户数据访问层
 * @author 9K
 * @create: 2023-07-21 13:11
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
