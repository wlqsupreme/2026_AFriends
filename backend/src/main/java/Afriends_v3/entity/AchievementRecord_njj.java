package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 成就记录实体类
 * 对应表: v3_achievement_record
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v3_achievement_record")
public class AchievementRecord_njj {
    @TableId(value = "record_id", type = IdType.INPUT)
    private Long recordId;
    private Long userId;
    private Long achievementId;
    private String progress;
    private Byte isCompleted;
    private Timestamp completedAt;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;
}
