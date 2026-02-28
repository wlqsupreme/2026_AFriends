package Interview_v3.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 用户反馈实体类
 * 对应表: user_feedback
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_feedback")
public class UserFeedback {
    @TableId(value = "feedback_id", type = IdType.AUTO)
    private Long feedbackId; // 反馈ID（主键）
    private Long userId; // 反馈用户ID
    private String feedbackType; // 反馈类型：功能建议、bug反馈、体验优化
    private String feedbackContent; // 反馈内容
    private String contactWay; // 联系方式（选填）
    private Byte handleStatus; // 处理状态：0-未处理 1-处理中 2-已处理
    private String handleResult; // 处理结果（管理员填写）
    private LocalDateTime createTime; // 反馈时间
    private LocalDateTime handleTime; // 处理时间
}