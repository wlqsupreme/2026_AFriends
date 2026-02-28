package Interview_v3.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 通用收藏表实体类
 * 对应表: collect
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("collect")
public class Collect {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id; // 收藏ID（主键）
    private Long userId; // 关联user.id（收藏用户）
    private Byte collectType; // 收藏类型：1-题库 2-题目 3-简历 4-企业 5-面试记录
    private Long targetId; // 目标ID（根据collect_type关联对应表主键）
    private String tag; // 收藏标签（如“高频题”“面经”）
    private Byte isTop; // 是否置顶：0-否 1-是
    private LocalDateTime collectTime; // 收藏时间
    private LocalDateTime updateTime; // 更新时间
    private Byte isDeleted; // 软删除标记（0-未删，1-已删）
}