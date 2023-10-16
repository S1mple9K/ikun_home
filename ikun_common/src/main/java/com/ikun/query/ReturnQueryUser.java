package com.ikun.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户列表整体返回实体类(包含总条数)
 * @author 9K
 * @create: 2023-07-21 14:29
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnQueryUser {
    /**
     * 用户列表
     */
    private List<QueryUser> queryUserList;

    /**
     * 总记录数
     */
    private Long total;
}
