package Afriends_v3.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * 用户关注实体类
 * 对应表: v3_user_follow_relationship
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v3_user_follow_relationship")
@ToString // 自动生成toString()，包含所有字段
public class UserFollowRelationship_zjx {
    @TableId(value = "follow_id", type = IdType.INPUT)
    private Long followId;
    private Long userId;
    private Long followedUserId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Integer isActive;
    @NotNull(message = "action不能为空")
    private String action;
}
