package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 我拥有物品评论实体类
 * 对应表: v2_i_have_comment
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_i_have_comment")
public class IHaveComment_wlq {
    @TableId(value = "ihave_comment_id", type = IdType.INPUT)
    private Long ihaveCommentId;
    private Long haveId;
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