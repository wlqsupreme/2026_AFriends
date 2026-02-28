package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 好友资料实体类
 * 对应表: v3_friends_profile
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v3_friends_profile")
public class FriendsProfile_njj {
    @TableId(value = "friends_profile_id", type = IdType.INPUT)
    private Long friendsProfileId;
    private Long userId;
    private Long friendId;
    private String friendsProfile;
    private Timestamp createdAt;
}
