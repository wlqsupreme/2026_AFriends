package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 用户小说推荐实体类
 * 对应表: v2_user_novel_recommendation
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_user_novel_recommendation")
public class UserNovelRecommendation_njj {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private Long userId;
    private Long novelContentId;
    private Float score;
    private Integer rankOrder;
    private Timestamp createdAt;
}
