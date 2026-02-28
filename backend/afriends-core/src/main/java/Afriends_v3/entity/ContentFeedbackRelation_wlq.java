package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 用户不喜欢内容关系实体类
 * 对应表: v2_content_feedback_relation
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_content_feedback_relation")
public class ContentFeedbackRelation_wlq {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private Long userId;
    private Long contentId;
    private Byte isDisliked;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}

