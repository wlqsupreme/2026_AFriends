package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 推荐标签实体类
 * 对应表: v2_recommended_label
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_recommended_label")
public class RecommendedLabel_wlq {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private String labelName;
    private String description;
    private Timestamp createdAt;
}
