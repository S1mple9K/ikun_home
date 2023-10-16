package com.ikun.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 修改isDelete状态值
 *
 * @author 9K
 * @create: 2023-07-21 16:17
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateIsDelete {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 逻辑删除值
     */
    private Integer isDelete;
}
