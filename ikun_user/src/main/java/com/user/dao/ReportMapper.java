package com.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ikun.pojo.Report;
import org.apache.ibatis.annotations.Mapper;

/**
 * 举报标签数据访问层
 * @author 9K
 * @create: 2023-07-29 14:16
 */
@Mapper
public interface ReportMapper extends BaseMapper<Report> {
}
