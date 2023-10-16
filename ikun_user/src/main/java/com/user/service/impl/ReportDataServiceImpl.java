package com.user.service.impl;

import com.ikun.pojo.ReportData;
import com.user.service.ReportDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * 举报信息业务逻辑层
 * @author 9K
 * @create: 2023-08-01 18:05
 */

@Service
public class ReportDataServiceImpl implements ReportDataService {

    /**
     * 注入MongoTemplate
     */
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 根据postId删除举报信息
     * @param postId
     */
    @Override
    public void deleteReportByPostId(String postId) {
        Query query = new Query(Criteria.where("post_id").is(postId));
        mongoTemplate.remove(query, ReportData.class);
    }
}
