package Afriends_v3.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * AI任务回应实体类
 * 对应表: v2_ai_task_respond
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_ai_task_respond")
public class AiTaskRespond_wlq {
    @TableId(value = "task_id", type = IdType.INPUT)
    private Long taskId;
    private String respondName;
    private String taskGroup;
    private BigDecimal requiredGiftPrice;
}

