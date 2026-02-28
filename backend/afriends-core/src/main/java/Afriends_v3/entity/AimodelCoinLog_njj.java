package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * AI模型币日志实体类
 * 对应表: v3_aimodel_coin_log
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v3_aimodel_coin_log")
public class AimodelCoinLog_njj {
    @TableId(value = "log_id", type = IdType.INPUT)
    private Long logId;
    private Long userId;
    private Long userAiId;
    private Long amount;
    private Long totalAfter;
    private Timestamp createdAt;
}
