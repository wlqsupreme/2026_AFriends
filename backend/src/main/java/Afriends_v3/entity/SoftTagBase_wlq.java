package Afriends_v3.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 软标签基础实体类
 * 对应表: v2_soft_tag_base
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_soft_tag_base")
public class SoftTagBase_wlq {
    @TableId(value = "soft_tag_id", type = IdType.INPUT)
    private Long softTagId;
    private String softTagContent;
    private String softTagVector;
    private Long categoryId;
    private Integer viewCount;
    private Integer likeCount;
    private Integer collectCount;
    private Integer commentCount;
    private BigDecimal relevanceValue;
    private Timestamp createdTime;
}
