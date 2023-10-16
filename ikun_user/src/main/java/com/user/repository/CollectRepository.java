package com.user.repository;

import com.ikun.pojo.Collect;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * 收藏表数据访问层
 * @author 9K
 * @create: 2023-07-27 15:12
 */

@Repository
public interface CollectRepository extends MongoRepository<Collect,String> {
}
