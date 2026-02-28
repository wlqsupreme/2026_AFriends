package Afriends_v3.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * 评论点赞关系实体类
 * 对应表: v2_comment_dislike_relation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_comment_dislike_relation")
public class CommentDislikeRelation_zjx {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long commentId;
    private Byte isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Byte type; // 类型（0-文字，1-图片，2-小说，3-ai模型）
}