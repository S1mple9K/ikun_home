package com.user.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云OOS配置类
 * @author 9K
 * @create: 2023-07-24 18:14
 */

@Configuration
public class OosConfig {

    private String endpoint="";

    private String accessKeyId="";

    private String accessKeySecret="";

    @Bean
    public OSS ossClient() {
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }
}
