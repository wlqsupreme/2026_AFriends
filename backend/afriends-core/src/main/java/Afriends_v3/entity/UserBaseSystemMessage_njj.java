package Afriends_v3.entity;

import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 用户基础系统消息实体类
 * 对应表: v3_user_base_system_message
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_user_base_system_message")
public class UserBaseSystemMessage_njj {
    @TableId(value = "msg_id", type = IdType.INPUT)
    private Long msgId;
    private Long userId;
    private Byte msgType;
    private String msgTitle;
    private String msgContent;
    private Byte relatedType;
    private Long relatedId;
    private Byte isRead;
    @TableLogic
    private Byte isDeleted;
    private Timestamp createdAt;
    private Timestamp readAt;

}
