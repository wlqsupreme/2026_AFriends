package Afriends_v3.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 图片帖子特征向量实体类
 * 对应表: v2_imagepost_feature_vector
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_imagepost_feature_vector")
public class ImagePostFeatureVector_wlq {
    @TableId(value = "post_id", type = IdType.INPUT)
    private Long postId;
    private String featureVector;
}
