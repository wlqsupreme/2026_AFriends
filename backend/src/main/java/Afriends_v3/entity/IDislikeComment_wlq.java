package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 我不喜欢内容评论实体类
 * 对应表: v2_i_dislike_comment
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_i_dislike_comment")
public class IDislikeComment_wlq {
    @TableId(value = "idislike_comment_id", type = IdType.INPUT)
    private Long idislikeCommentId;
    private Long dislikeId;
    private Long userId;
    private Long parentCommentId;
    private String commentText;
    private String imageUrls;
    private Integer likeCount;
    @TableLogic
    private Byte isDeleted;
    private Timestamp deletedAt;
    private Timestamp createdAt;
}