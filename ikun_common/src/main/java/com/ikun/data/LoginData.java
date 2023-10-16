package com.ikun.data;

import lombok.Data;

/**
 * 密码登录对应实体类
 *
 * @author 9K
 * @create: 2023-07-23 16:01
 */

@Data
public class LoginData {
    private String phone;
    private String password;
}
