package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 评论点赞关系实体类
 * 对应表: v2_comment_like_relation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_comment_like_relation")
public class CommentLikeRelation_wlq {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    private Long commentId;
    private Byte isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Byte type; // 类型（0-文字，1-图片，2-小说，3-ai模型）
}