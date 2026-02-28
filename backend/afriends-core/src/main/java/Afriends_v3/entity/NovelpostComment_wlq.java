package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 小说评论实体类
 * 对应表: v2_novelpost_comment
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_novelpost_comment")
public class NovelpostComment_wlq {
    @TableId(value = "novelpost_comment_id", type = IdType.INPUT)
    private Long novelpostCommentId;

    private Long novelId;
    private Long userId;
    private Long parentCommentId;
    private String commentText;
    private String imageUrls; // JSON格式的图片URLs
    private Integer likeCount;
    private Integer dislikeCount;
    private Byte isDeleted;
    private Timestamp deletedAt;
    private Timestamp createdAt;
}