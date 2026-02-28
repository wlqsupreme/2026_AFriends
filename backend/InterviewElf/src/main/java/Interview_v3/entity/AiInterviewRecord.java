package Interview_v3.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * AI模拟面试记录表实体类
 * 对应表: ai_interview_record
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("ai_interview_record")
public class AiInterviewRecord {
    @TableId(value = "interview_id", type = IdType.AUTO)
    private Long interviewId; // 面试记录ID（主键）
    private Long userId; // 关联user.id（面试用户）
    private Long bankId; // 关联question_bank.bank_id（使用的题库ID）
    private Byte interviewStatus; // 面试状态：1-进行中 2-已完成 3-已中断
    private Integer totalScore; // AI综合评分（0-100）
    private Integer questionCount; // 答题总数
    private Integer correctCount; // 答对题数
    private LocalDateTime startTime; // 面试开始时间
    private LocalDateTime endTime; // 面试结束时间
    private String answerDetail; // 答题详情（JSON格式）
    private Byte isDeleted; // 软删除标记：0-未删 1-已删
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
}