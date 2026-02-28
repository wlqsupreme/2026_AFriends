package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 文字帖子评论实体类
 * 对应表: v2_textpost_comment
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_textpost_comment")
public class TextpostComment_wlq {
    @TableId(value = "textpost_comment_id", type = IdType.INPUT)
    private Long textpostCommentId;
    private Long userId;
    private Long parentCommentId;
    private String commentText;
    private Integer likeCount;
    private Byte isVisible;
    private Byte status;
    @TableLogic
    private Byte isDeleted;
    private Timestamp deletedAt;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Long textContentId;
}