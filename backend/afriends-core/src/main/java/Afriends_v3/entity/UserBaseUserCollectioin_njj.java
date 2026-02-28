package Afriends_v3.entity;

import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 用户基础用户收藏实体类
 * 对应表: v3_user_base_user_collection
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_user_base_user_collection")
public class UserBaseUserCollectioin_njj {
    @TableId(value = "rel_id", type = IdType.INPUT)
    private Long relId;
    private Timestamp collectionTime;
    private String coverUrl;
    private Long folderId;
    @TableLogic
    private Integer isDeleted;
    private String remark;
    private Long targetId;
    private Integer targetType;
    private String title;
    private Timestamp updateTime;
    private Long userId;
}
