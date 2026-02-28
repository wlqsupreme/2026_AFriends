package Afriends_v3.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * 用户聊天通话详情实体类
 * 对应表: v2_user_chat_call
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_user_chat_call")
public class UserChatCall_njj {
    private Long id;
    private Long sessionId;
    private String callerId; // 呼叫者ID
    private String calleeId; // 被呼叫者ID
    private String callType; // voice/video
    private String callStatus; // pending/accept/reject/finish
    private Timestamp startTime;
    private Timestamp endTime;
    private Integer duration; // 通话时长(秒)
}
