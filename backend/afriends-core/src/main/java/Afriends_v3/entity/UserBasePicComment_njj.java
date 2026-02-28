package Afriends_v3.entity;

import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 用户与图片评论的关系实体类
 * 对应表: v3_user_base_pic_comment
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_user_base_pic_comment")
public class UserBasePicComment_njj {

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
