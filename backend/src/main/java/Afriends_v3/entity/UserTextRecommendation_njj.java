package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 用户文本推荐实体类
 * 对应表: v2_user_text_recommendation
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_user_text_recommendation")
public class UserTextRecommendation_njj {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private Long userId;
    private Long textContentId;
    private Float score;
    private Integer rankOrder;
    private Timestamp createdAt;
}
