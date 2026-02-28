package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 用户系统消息实体类
 * 对应表: v2_user_system_message
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_user_system_message")
public class UserSystemMessage_njj {
    @TableId(value = "message_id", type = IdType.INPUT)
    private Long messageId;
    private Long recipientUserId;
    private Long senderUserId;
    private String messageContent;
    private Byte isRead;
    private String relatedEntityType;
    private Long relatedEntityId;
    private Timestamp createdAt;

}
