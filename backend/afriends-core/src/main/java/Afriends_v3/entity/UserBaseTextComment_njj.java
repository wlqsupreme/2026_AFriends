package Afriends_v3.entity;

import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 用户基础文本评论实体类
 * 对应表: v3_user_base_text_comment
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_user_base_text_comment")
public class UserBaseTextComment_njj {
    @TableId(value = "rel_id", type = IdType.INPUT)
    private Long relId;
    private Long commentId;
    private Timestamp createdAt;
    @TableLogic
    private Integer isDeleted;
    private String label;
    private Timestamp updatedAt;
    private Long userId;

}
