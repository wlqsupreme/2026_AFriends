package Afriends_v3.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * AI任务需求实体类
 * 对应表: v2_ai_task_require
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_ai_task_require")
public class AiTaskRequire_wlq {
    @TableId(value = "task_id", type = IdType.INPUT)
    private Long taskId;
    private String taskName;
    private String taskGroup;
}

