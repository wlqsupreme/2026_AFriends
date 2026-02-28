package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 用户需求评论实体类
 * 对应表: v2_i_need_comment
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_i_need_comment")
public class INeedComment_wlq {
    @TableId(value = "ineed_comment_id", type = IdType.INPUT)
    private Long ineedCommentId;
    private Long needId;
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