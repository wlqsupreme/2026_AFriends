package Interview_v3.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 题库信息表实体类
 * 对应表: question_bank
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("question_bank")
public class QuestionBank {
    @TableId(value = "bank_id", type = IdType.AUTO)
    private Long bankId; // 题库ID（主键）
    private String bankName; // 题库名称
    private BigDecimal price; // 原始价格（元）
    private BigDecimal currentPrice; // 当前价格（元）
    private String tag; // 标签（如“高频”“高级”）
    private Long categoryId; // 归属分类ID
    private Byte status; // 状态（1-启用，0-禁用）
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
    private Byte isDeleted; // 软删除标记（0-未删，1-已删）
    private Integer questionCount; // 题库题目总数
    private BigDecimal avgScore; // 题库平均评分（1-5分）
    private String intro; // 题库简介
    private String coverUrl; // 题库封面图URL
}