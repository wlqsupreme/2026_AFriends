package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 推荐昵称实体类
 * 对应表: v2_recommended_nickname
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_recommended_nickname")
public class RecommendedNickname_wlq {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private String nickname;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
