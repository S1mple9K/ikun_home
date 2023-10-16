package com.ikun.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 举报标签实体类
 * @author 9K
 * @create: 2023-07-29 14:14
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 举报标签名称
     */
    private String name;
}
