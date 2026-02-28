package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 好友列表实体类
 * 对应表: friend_list
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("friend_list")
public class FriendList_wlq {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private Long friendId;
    private String friendName;
    private String avatarUrl;
    private String status;
    private Timestamp lastActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}

