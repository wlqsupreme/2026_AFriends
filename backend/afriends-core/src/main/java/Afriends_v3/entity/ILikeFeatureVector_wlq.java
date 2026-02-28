package Afriends_v3.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 我喜欢内容特征向量实体类
 * 对应表: v2_i_like_feature_vector
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_i_like_feature_vector")
public class ILikeFeatureVector_wlq {
    @TableId(value = "like_id", type = IdType.INPUT)
    private Long likeId;
    private String featureVector;
}
