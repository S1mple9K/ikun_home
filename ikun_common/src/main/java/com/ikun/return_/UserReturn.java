package com.ikun.return_;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录成功返回
 * @author 9K
 * @create: 2023-07-23 14:56
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserReturn {
    private String userId;
    private String nickName;
    private String password;
    private String photo;
    /**
     * 标记是否为新用户
     */
    private Integer isFirst;

    /**
     * 当前用户状态
     */
    private Integer status;

}
