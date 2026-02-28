package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * AI任务日志实体类
 * 对应表: v3_ai_task_log
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v3_ai_task_log")
public class AiTaskLog_njj {
    @TableId(value = "ai_task_log_id", type = IdType.INPUT)
    private Long aiTaskLogId;
    private String aiTaskName;
    private String aiTaskDesc;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Byte status;
    private String taskComment;
    private String taskStar;
}
