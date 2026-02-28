package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 软标签分类实体类
 * 对应表: v2_soft_tag_category
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_soft_tag_category")
public class SoftTagCategory_wlq {
    @TableId(value = "soft_category_id", type = IdType.INPUT)
    private Long softCategoryId;
    private String softCategoryContent;
    private Long parentSoftCategoryId;
    private Integer level;
    private Timestamp createdAt;
    private String backup;
    private String keyvalue;
    private String featureVector;
}
