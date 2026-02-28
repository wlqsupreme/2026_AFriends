package Afriends_v3.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 用户AI需求特征向量实体类
 * 对应表: v2_user_ai_require_feature_vector
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_user_ai_require_feature_vector")
public class UserAiRequireFeatureVector_wlq {
    @TableId(value = "post_id", type = IdType.INPUT)
    private Long postId;
    private String featureVector;
}
