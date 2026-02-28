package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 用户好友关系实体类
 * 对应表: v3_user_friends_relationship
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v3_user_friends_relationship")
public class UserFriendsRelationship_njj {
    @TableId(value = "user_friends_info_id", type = IdType.INPUT)
    private Long userFriendsInfoId;
    private Long userId;
    private Long friendsId;
    private Long functionId;
    private String functionSettings;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
