package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 用户点赞关系实体类
 * 对应表: v2_user_like_relation
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_user_like_relation")
public class UserLikeRelation_njj {
    @TableId(value = "like_id", type = IdType.INPUT)
    private Long likeId;
    private Long likerUserId;
    private Long likedUserId;
    private Byte isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
