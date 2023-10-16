package com.ikun.return_;

import com.ikun.pojo.ReportData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 举报列表详细返回封装实体类
 * @author 9K
 * @create: 2023-07-29 15:46
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDataReturn {

    /**
     * 举报信息
     */
    private ReportData reportData;

    /**
     * 帖子信息
     */
    private PostReturn postReturn;
}
