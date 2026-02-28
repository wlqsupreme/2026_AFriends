package Afriends_v3.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 小说帖子特征向量实体类
 * 对应表: v2_novelpost_feature_vector
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_novelpost_feature_vector")
public class NovelpostFeatureVector_wlq {
    @TableId(value = "novel_id", type = IdType.INPUT)
    private Long novelId;
    private String featureVector;
}
