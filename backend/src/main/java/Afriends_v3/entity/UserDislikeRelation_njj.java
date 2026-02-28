package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 用户不喜欢关系实体类
 * 对应表: v2_user_dislike_relation
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_user_dislike_relation")
public class UserDislikeRelation_njj {
    @TableId(value = "dislike_id", type = IdType.INPUT)
    private Long dislikeId;
    private Long dislikerUserId;
    private Long dislikedUserId;
    private Byte isActive;
    private Timestamp createAt;
    private Timestamp updateAt;
}
