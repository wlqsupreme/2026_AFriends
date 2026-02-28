package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 用户AI评论实体类
 * 对应表: v3_user_ai_comment
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v3_user_ai_comment")
public class UserAiComment_njj {
    @TableId(value = "user_ai_comment_id", type = IdType.INPUT)
    private Long userAiCommentId;
    private Long aimodelId;
    private Long userId;
    private String commentText;
    private Long likeCount;
    private Byte isVisible;
    private Byte stars;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
