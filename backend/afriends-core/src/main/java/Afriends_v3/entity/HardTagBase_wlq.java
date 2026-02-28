package Afriends_v3.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 标签核心元数据实体类
 * 对应表: v2_hard_tag_base
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_hard_tag_base")
public class HardTagBase_wlq {
    @TableId(value = "hard_tag_id", type = IdType.INPUT)
    private Long hardTagId;
    private String hardTagContent;
    private String hardTagVector;
    private Long categoryId;
    private Integer viewCount;
    private Integer likeCount;
    private Integer collectCount;
    private Integer commentCount;
    private BigDecimal relevanceValue;
    private Timestamp createdTime;
}

