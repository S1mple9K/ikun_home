package com.ikun.return_;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 关注人列表
 * @author 9K
 * @create: 2023-07-28 14:31
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareShowReturn {
    private String nickName;
    private String photo;
}
