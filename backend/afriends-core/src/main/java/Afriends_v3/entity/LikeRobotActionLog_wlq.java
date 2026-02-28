package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 点赞机器人操作日志实体类
 * 对应表: v2_like_robot_action_log
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_like_robot_action_log")
public class LikeRobotActionLog_wlq {
    @TableId(value = "log_id", type = IdType.INPUT)
    private Long logId;
    private Long userId;
    private Long postId;
    private String actionType;
    private Integer score;
    private Timestamp createdAt;
}
