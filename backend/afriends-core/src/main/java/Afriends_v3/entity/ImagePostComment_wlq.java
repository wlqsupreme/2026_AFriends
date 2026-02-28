package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 图片帖子评论实体类
 * 对应表: v2_imagepost_comment
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_imagepost_comment")
public class ImagePostComment_wlq {
    @TableId(value = "imagepost_comment_id", type = IdType.INPUT)
    private Long imagepostCommentId;
    private Long postId;
    private Long userId;
    private Long parentCommentId;
    private String commentText;
    private String imageUrls;
    private Integer likeCount;
    private Byte isVisible;
    private Byte status;
    @TableLogic
    private Byte isDeleted;
    private Timestamp deletedAt;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Long targetImageId;
}