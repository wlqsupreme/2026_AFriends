package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 用户基础实体类
 * 对应表: v2_user_base
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_user_base")
public class UserBase_wlq {
    @TableId(value = "user_id", type = IdType.INPUT)
    private Long userId;
    private String loginTelAccount;
    private String loginAccount;
    private String loginWechatAccount;
    private String loginQqAccount;
    private String boundDouyinAccount;
    private String machineCodes;
    private String passwordHash;
    private Timestamp createdAt;
    private Timestamp lastActive;
    private String userKind;
    @TableLogic
    private Integer deleted;
}
