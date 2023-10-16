package com.ikun.add;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户初次登录/注册
 * @author 9K
 * @create: 2023-07-23 14:15
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddUser {
     private String nickName;
     private String phone;
     private String password;
     private String createTime;
}
