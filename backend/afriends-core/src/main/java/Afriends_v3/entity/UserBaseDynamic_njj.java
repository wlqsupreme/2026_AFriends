package Afriends_v3.entity;

import java.sql.Timestamp;

/**
 * 用户动态基础信息实体类
 * 对应表: v3_user_base_dynamic
 */
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_user_base_dynamic")
public class UserBaseDynamic_njj {

    @TableId(value = "dynamic_id", type = IdType.INPUT)
    private Long dynamicId; // 主键ID
    private Long userId; // 用户ID
    private String content; // 动态内容
    private Byte contentType; // 内容类型（1=文本，2=图片，3=视频）
    private Long originalDynamicId; // 原始动态ID（转发时使用）
    private Integer forwardCount; // 转发数
    private Integer commentCount; // 评论数
    private Integer likeCount; // 点赞数
    private Byte isVisible; // 是否可见（1=公开，2=好友可见，3=仅自己可见）

    @TableLogic
    private Byte isDeleted;

    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String location; // 位置
}
