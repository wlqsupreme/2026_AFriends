package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 用户聊天列表实体类
 * 对应表: v3_user_chat_list
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_user_chat_list_r")
public class UserChatList_njj {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private Long userId;
    private Long friendId;
    private String friendName;
    private String avatarUrl;
    private Long sessionId;
    private String lastMessage;
    private Timestamp lastMessageTime;
    private Integer unreadCount;
    private String status;
    private Timestamp lastActive;
    @TableField("created_at")
    private Timestamp createdAt;
    @TableField("updated_at")
    private Timestamp updatedAt;

}