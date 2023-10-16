package com.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ikun.pojo.Report;
import com.user.dao.ReportMapper;
import com.user.service.ReportService;
import org.springframework.stereotype.Service;

/**
 * 举报标签业务逻辑层
 * @author 9K
 * @create: 2023-07-29 14:15
 */

@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {
}
