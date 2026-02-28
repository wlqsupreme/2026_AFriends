package Afriends_v3.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 用户需求特征向量实体类
 * 对应表: v2_i_need_feature_vector
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_i_need_feature_vector")
public class INeedFeatureVector_wlq {
    @TableId(value = "need_id", type = IdType.INPUT)
    private Long needId;
    private String featureVector;
}
