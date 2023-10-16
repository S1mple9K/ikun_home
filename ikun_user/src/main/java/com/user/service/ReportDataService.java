package com.user.service;

/**
 * 举报信息业务逻辑接口层
 * @author 9K
 * @create: 2023-08-01 18:04
 */
public interface ReportDataService {

    /**
     * 根据postId删除举报信息
     * @param postId
     */
    void deleteReportByPostId(String postId);
}
