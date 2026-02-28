package Afriends_v3.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 我拥有物品特征向量实体类
 * 对应表: v2_i_have_feature_vector
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_i_have_feature_vector")
public class IHaveFeatureVector_wlq {
    @TableId(value = "have_id", type = IdType.INPUT)
    private Long haveId;
    private String featureVector;
}
