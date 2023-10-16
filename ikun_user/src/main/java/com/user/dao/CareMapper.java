package com.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ikun.pojo.Care;
import org.apache.ibatis.annotations.Mapper;

/**
 * 关注表数据访问层
 * @author 9K
 * @create: 2023-07-28 13:39
 */

@Mapper
public interface CareMapper extends BaseMapper<Care> {
}
