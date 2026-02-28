package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 推荐头像实体类
 * 对应表: v2_recommended_avatar
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_recommended_avatar")
public class RecommendedAvatar_wlq {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private String url;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
