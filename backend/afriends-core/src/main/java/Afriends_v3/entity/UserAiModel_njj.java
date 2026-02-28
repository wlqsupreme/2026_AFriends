package Afriends_v3.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 用户AI模型实体类
 * 对应表: v3_user_ai_model
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v3_user_ai_model")
public class UserAiModel_njj {
    @TableId(value = "user_ai_id", type = IdType.INPUT)
    private Long userAiId;
    private Long userId;
    private Long parentModelId;
    private String parentModelVersion;
    private String modelName;
    private String customDesc;
    private String modelImageUrl;
    private String tone;
    private String questionWeight;
    private String questionContent;
    private Integer power;
    private Integer level;
    private Integer totalExp;
    private BigDecimal rechargeAmount;
    private Byte isVisible;
    private Byte status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;
    private Timestamp tryOutAt;
    private Long tryOutNum;
}
