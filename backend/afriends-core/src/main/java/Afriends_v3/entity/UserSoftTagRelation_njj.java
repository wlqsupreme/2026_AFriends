package Afriends_v3.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * 用户软标签关系实体类
 * 对应表: v3_user_soft_tag_relation
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_user_soft_tag_relation")
public class UserSoftTagRelation_njj {
    @TableId(value = "soft_relation_id", type = IdType.INPUT)
    private Long softRelationId;
    private Long userId;
    private Long softTagId;
    private BigDecimal softRelevanceScore;
    private Timestamp lastUpdated;
    private Timestamp createdTime;
}
