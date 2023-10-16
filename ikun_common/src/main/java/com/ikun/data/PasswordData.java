package com.ikun.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户修改密码实体类
 * @author 9K
 * @create: 2023-07-24 16:00
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordData {
    /**
     * 用户id
     */
    private String userId;

    /**
     * 旧密码
     */
    private String oldPwd;

    /**
     * 新密码
     */
    private String newPwd;
}
