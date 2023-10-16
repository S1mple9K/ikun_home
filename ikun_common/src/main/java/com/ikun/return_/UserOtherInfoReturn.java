package com.ikun.return_;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户其他信息返回类:获赞 关注 粉丝 贴子数
 * @author 9K
 * @create: 2023-10-11 18:08
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOtherInfoReturn {
    /**
     * 贴子数
     */
    private Integer postNumber;

    /**
     * 获赞数
     */
    private Integer likeNumber;

    /**
     * 关注数
     */
    private Integer careNumber;

    /**
     * 粉丝数
     */
    private Integer fansNumber;
}
