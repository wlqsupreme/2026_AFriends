package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 用户任务关系实体类
 * 对应表: v3_user_task_relationship
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v3_user_task_relationship")
public class UserTaskRelationship_njj {
    @TableId(value = "user_task_id", type = IdType.INPUT)
    private Long userTaskId;
    private Long userId;
    private Byte status;
    private Byte taskPercent;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp endedAt;
}
