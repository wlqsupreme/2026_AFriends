package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 任务评论实体类
 * 对应表: v3_task_comment
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v3_task_comment")
public class TaskComment_njj {
    @TableId(value = "task_comment_id", type = IdType.INPUT)
    private Long taskCommentId;
    private Long userId;
    private Byte stars;
    private String commentText;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Byte isVisible;
}
