package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 我喜欢内容评论实体类
 * 对应表: v2_i_like_comment
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_i_like_comment")
public class ILikeComment_wlq {
    @TableId(value = "ilike_comment_id", type = IdType.INPUT)
    private Long ilikeCommentId;
    private Long likeId;
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