package com.user.repository;

import com.ikun.pojo.ReportData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * 举报信息数据访问层
 * @author 9K
 * @create: 2023-07-29 14:44
 */
@Repository
public interface ReportDataRepository extends MongoRepository<ReportData,Long> {
}
