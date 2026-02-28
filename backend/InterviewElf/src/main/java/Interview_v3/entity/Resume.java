package Interview_v3.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 简历表实体类
 * 对应表: resume
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("resume")
public class Resume {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id; // 简历ID（主键）
    private Long userId; // 关联user.id（所属用户）
    private String resumeName; // 简历名称
    private String realName; // 真实姓名
    private String gender; // 性别：男/女
    private Integer age; // 年龄
    private String phone; // 联系电话
    private String email; // 邮箱
    private String education; // 学历：本科/硕士/博士等
    private String workExperience; // 工作经验（JSON格式）
    private String projectExperience; // 项目经验（JSON格式）
    private String skill; // 技能标签（逗号分隔）
    private Byte isDefault; // 是否默认简历：0-否 1-是
    private Byte isDeleted; // 软删除标记：0-未删 1-已删
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
}