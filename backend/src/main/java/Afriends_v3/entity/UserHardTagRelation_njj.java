package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 用户硬标签关系实体类
 * 对应表: v2_user_hard_tag_relation
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_user_hard_tag_relation")
public class UserHardTagRelation_njj {
    @TableId(value = "hard_relation_id", type = IdType.INPUT)
    private Long hardRelationId;
    private Long userId;
    private Long hardTagId;
    private Timestamp lastUpdated;
    private Timestamp createdTime;
}
