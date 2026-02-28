package Interview_v3.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 题库分类实体类
 * 对应表: question_bank_category
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("question_bank_category")
public class QuestionBankCategory {
    @TableId(value = "category_id", type = IdType.AUTO)
    private Long categoryId; // 分类ID（主键）
    private String categoryName; // 分类名称（如：Java、Python、前端）
    private String categoryDesc; // 分类描述
    private Integer sort; // 排序权重（数字越小越靠前）
    private Byte isDeleted; // 软删除：0-未删 1-已删
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
}