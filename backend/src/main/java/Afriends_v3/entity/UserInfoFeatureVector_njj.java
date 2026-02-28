package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 用户信息特征向量实体类
 * 对应表: v2_user_info_feature_vector
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_user_info_feature_vector")
public class UserInfoFeatureVector_njj {
    @TableId(value = "user_id", type = IdType.INPUT)
    private Long userId;
    private String featureVector;
}
