package com.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ikun.pojo.Care;
import com.user.dao.CareMapper;
import com.user.service.CareService;
import org.springframework.stereotype.Service;

/**
 * 关注表业务逻辑层
 * @author 9K
 * @create: 2023-07-28 13:39
 */
@Service
public class CareServiceImpl extends ServiceImpl<CareMapper, Care> implements CareService {
}
