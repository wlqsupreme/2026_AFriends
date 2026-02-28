package Afriends_v3.entity;

import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 用户基础点赞行为实体类
 * 对应表: v3_user_base_like_action
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_user_base_like_action")
public class UserBaseLikeAction_njj {

    @TableId(value = "like_id", type = IdType.INPUT)
    private Long likeId;
    private Long userId;
    private Integer targetType;
    private Long targetId;
    private Timestamp likeTime;
    @TableLogic
    private Byte isCanceled;


}
