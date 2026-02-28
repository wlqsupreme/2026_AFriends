package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 成就基础实体类
 * 对应表: v3_achievement_base
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v3_achievement_base")
public class AchievementBase_njj {
    @TableId(value = "achievement_id", type = IdType.INPUT)
    private Long achievementId;
    private String iconUrl;
    private String name;
    private String description;
    @TableField("`condition`")
    private String condition;
    private Byte isView;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;
}
