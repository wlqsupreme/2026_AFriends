package Afriends_v3.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * AI模型等级规则实体类
 * 对应表: v3_aimodel_level_rule
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v3_aimodel_level_rule")
public class AimodelLevelRule_njj {
    @TableId(value = "level_id", type = IdType.INPUT)
    private Long levelId;
    private Integer level;
    private Double powerBonus;
    private Integer expRequirement;
    private Long tempLevelIdReinde;
}