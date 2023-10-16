package com.ikun.data;

import lombok.Data;

/**
 * 用户意见封装类
 * @author 9K
 * @create: 2023-10-14 14:45
 */

@Data
public class OpinionData {
    private String userId;
    private String nickName;
    /**
     * 建议信息
     */
    private String opinion;
}
