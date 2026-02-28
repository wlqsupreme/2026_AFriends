package Afriends_v3.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * AI模型基础信息实体类
 * 对应表: v3_aimodel_base_info
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v3_aimodel_base_info")
public class AimodelBaseInfo_njj {
    @TableId(value = "model_id", type = IdType.INPUT)
    private Long modelId;
    private String modelName;
    private String modelDesc;
    private String modelImageUrl;
    private String tone;
    private String questionWeight;
    private String questionContent;
    private Integer power;
    private BigDecimal price;
    private Integer purchaseCount;
    private Integer recommendCount;
    private BigDecimal score;
    private Integer likeCount;
    private Integer commentCount;
    private Integer collectCount;
    private Integer dislikeCount;
    private Integer reportCount;
    @TableLogic
    private Byte isVisible;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;
    private Byte status;
    private String version;
    private String commentDetail;
    private Long tempModelIdReind;
    private Long tryOutNum;
}
