package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 用户评论基础实体类
 * 对应表: v2_user_review_base
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_user_review_base")
public class UserReviewBase_njj {
    @TableId(value = "user_review_id", type = IdType.INPUT)
    private Long userReviewId;
    private Long reviewerUserId;
    private Long targetUserId;
    private String reviewText;
    private Byte ratingScore;
    private Integer likeCount;
    private Byte isAnonymous;
    private Byte isVisible;
    private Byte status;
    @TableLogic
    private Byte isDeleted;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;
}