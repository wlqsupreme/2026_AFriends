package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * AI聊天历史实体类
 * 对应表: v2_ai_chat_list_detail_r
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_ai_chat_list_detail_r")
public class AiChatListDetailR_wlq {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private Long userId;
    private Long userAiId;
    private String userMessage;
    private String aiResponse;
    private String hardTags;
    private String softTags;
    private String featureVector;
    private String chatType;
    private Timestamp messageTimestamp;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}

